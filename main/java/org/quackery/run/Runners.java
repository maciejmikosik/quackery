package org.quackery.run;

import static org.quackery.QuackeryException.check;
import static org.quackery.Tests.deep;
import static org.quackery.Tests.ifCase;
import static org.quackery.Tests.onBody;
import static org.quackery.common.ExecutorBuilder.executorBuilder;
import static org.quackery.common.Interrupter.interrupter;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

import org.quackery.Body;
import org.quackery.Test;
import org.quackery.common.Interrupter;
import org.quackery.report.AssertException;

public class Runners {
  public static Test run(Test root) {
    check(root != null);
    return deep(ifCase(onBody(Runners::run)))
        .apply(root);
  }

  private static Body run(Body body) {
    try {
      body.run();
    } catch (Throwable throwable) {
      return () -> {
        throw throwable;
      };
    }
    return () -> {};
  }

  public static Test in(Executor executor, Test root) {
    check(root != null);
    check(executor != null);
    return deep(ifCase(onBody(body -> futureBody(executor, body))))
        .apply(root);
  }

  private static Body futureBody(Executor executor, Body body) {
    FutureTask<Body> future = new FutureTask<Body>(() -> run(body));
    executor.execute(future);
    return () -> future.get().run();
  }

  public static Test concurrent(Test test) {
    Executor executor = executorBuilder()
        .poolSize(Runtime.getRuntime().availableProcessors())
        .keepAlive(Duration.ofNanos(1))
        .allowCoreThreadTimeOut(true)
        .build();
    return in(executor, test);
  }

  public static Test expect(Class<? extends Throwable> throwable, Test test) {
    check(test != null);
    return deep(ifCase(onBody(body -> expect(throwable, body))))
        .apply(test);
  }

  private static Body expect(Class<? extends Throwable> expected, Body body) {
    return () -> {
      Throwable thrown = null;
      try {
        body.run();
      } catch (Throwable throwable) {
        thrown = throwable;
      }
      if (thrown == null) {
        throw new AssertException("nothing thrown");
      }
      if (!expected.isAssignableFrom(thrown.getClass())) {
        throw new AssertException(thrown);
      }
    };
  }

  public static Test timeout(Duration duration, Test test) {
    check(!duration.isNegative());
    check(test != null);
    return deep(ifCase(onBody(body -> timeout(duration, body, interrupter()))))
        .apply(test);
  }

  private static Body timeout(Duration duration, Body body, Interrupter interrupter) {
    return () -> {
      Future<?> alarm = interrupter.interruptMe(duration);
      try {
        body.run();
      } finally {
        alarm.cancel(true);
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
      }
    };
  }

  public static Test threadScoped(Test root) {
    check(root != null);
    return deep(ifCase(onBody(Runners::threadScoped)))
        .apply(root);
  }

  private static Body threadScoped(Body body) {
    return () -> {
      AtomicReference<Throwable> throwable = new AtomicReference<>(null);
      Thread thread = new Thread(new Runnable() {
        public void run() {
          try {
            body.run();
          } catch (Throwable t) {
            throwable.set(t);
          }
        }
      });
      thread.start();
      try {
        thread.join();
      } catch (InterruptedException e) {
        thread.interrupt();
        thread.join();
        throw e;
      }
      if (throwable.get() != null) {
        throw throwable.get();
      }
    };
  }

  public static Test classLoaderScoped(Test root) {
    check(root != null);
    return deep(ifCase(onBody(Runners::classLoaderScoped)))
        .apply(root);
  }

  private static Body classLoaderScoped(Body body) {
    return () -> {
      Thread thread = Thread.currentThread();
      ClassLoader original = thread.getContextClassLoader();
      thread.setContextClassLoader(new ClassLoader(original) {});
      try {
        body.run();
      } finally {
        thread.setContextClassLoader(original);
      }
    };
  }
}
