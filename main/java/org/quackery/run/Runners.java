package org.quackery.run;

import static org.quackery.QuackeryException.check;
import static org.quackery.Tests.deep;
import static org.quackery.Tests.ifCase;
import static org.quackery.Tests.onScript;
import static org.quackery.common.ExecutorBuilder.executorBuilder;
import static org.quackery.common.Interrupter.interrupter;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

import org.quackery.Script;
import org.quackery.Test;
import org.quackery.common.Interrupter;
import org.quackery.report.AssertException;

public class Runners {
  public static Test run(Test root) {
    check(root != null);
    return deep(ifCase(onScript(Runners::run)))
        .apply(root);
  }

  private static Script run(Script script) {
    try {
      script.run();
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
    return deep(ifCase(onScript(script -> futureScript(executor, script))))
        .apply(root);
  }

  private static Script futureScript(Executor executor, Script script) {
    FutureTask<Script> future = new FutureTask<Script>(() -> run(script));
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
    return deep(ifCase(onScript(script -> expect(throwable, script))))
        .apply(test);
  }

  private static Script expect(Class<? extends Throwable> expected, Script script) {
    return () -> {
      Throwable thrown = null;
      try {
        script.run();
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
    return deep(ifCase(onScript(script -> timeout(duration, script, interrupter()))))
        .apply(test);
  }

  private static Script timeout(Duration duration, Script script, Interrupter interrupter) {
    return () -> {
      Future<?> alarm = interrupter.interruptMe(duration);
      try {
        script.run();
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
    return deep(ifCase(onScript(Runners::threadScoped)))
        .apply(root);
  }

  private static Script threadScoped(Script script) {
    return () -> {
      AtomicReference<Throwable> throwable = new AtomicReference<>(null);
      Thread thread = new Thread(new Runnable() {
        public void run() {
          try {
            script.run();
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
    return deep(ifCase(onScript(Runners::classLoaderScoped)))
        .apply(root);
  }

  private static Script classLoaderScoped(Script script) {
    return () -> {
      Thread thread = Thread.currentThread();
      ClassLoader original = thread.getContextClassLoader();
      thread.setContextClassLoader(new ClassLoader(original) {});
      try {
        script.run();
      } finally {
        thread.setContextClassLoader(original);
      }
    };
  }
}
