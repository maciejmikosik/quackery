package org.quackery.run;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.quackery.QuackeryException.check;
import static org.quackery.help.Helpers.failingCase;
import static org.quackery.help.Helpers.successfulCase;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.quackery.Case;
import org.quackery.Test;
import org.quackery.help.TraversingDecorator;

public class Runners {
  public static Test run(Test root) {
    check(root != null);
    return new TraversingDecorator() {
      protected Test decorateCase(Case cas) {
        return run(cas);
      }
    }.decorate(root);
  }

  private static Case run(Case cas) {
    try {
      cas.run();
    } catch (Throwable throwable) {
      return failingCase(cas.name, throwable);
    }
    return successfulCase(cas.name);
  }

  public static Test in(final Executor executor, Test root) {
    check(root != null);
    check(executor != null);
    return new TraversingDecorator() {
      protected Case decorateCase(Case cas) {
        return futureCase(executor, cas);
      }
    }.decorate(root);
  }

  public static Test concurrent(Test test) {
    return in(concurrentExecutor, test);
  }

  private static final ExecutorService concurrentExecutor = concurrentExecutor();

  private static ThreadPoolExecutor concurrentExecutor() {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    int corePoolSize = availableProcessors;
    int maximumPoolSize = availableProcessors;
    int keepAliveTime = 1;
    TimeUnit keepAliveTimeUnit = NANOSECONDS;
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        corePoolSize, maximumPoolSize,
        keepAliveTime, keepAliveTimeUnit,
        new LinkedBlockingQueue<Runnable>());
    executor.allowCoreThreadTimeOut(true);
    return executor;
  }

  private static Case futureCase(Executor executor, final Case test) {
    final FutureTask<Case> future = new FutureTask<Case>(new Callable<Case>() {
      public Case call() {
        return run(test);
      }
    });
    executor.execute(future);
    return new Case(test.name) {
      public void run() throws Throwable {
        future.get().run();
      }
    };
  }

  public static Test threadScoped(Test root) {
    check(root != null);
    return new TraversingDecorator() {
      protected Test decorateCase(Case cas) {
        return threadScoped(cas);
      }
    }.decorate(root);
  }

  private static Case threadScoped(final Case cas) {
    return new Case(cas.name) {
      private Throwable throwable;

      public void run() throws Throwable {
        Thread thread = new Thread(new Runnable() {
          public void run() {
            try {
              cas.run();
            } catch (Throwable t) {
              throwable = t;
            }
          }
        });
        thread.start();
        thread.join();
        if (throwable != null) {
          throw throwable;
        }
      }
    };
  }

  public static Test classLoaderScoped(Test root) {
    check(root != null);
    return new TraversingDecorator() {
      protected Case decorateCase(Case cas) {
        return classLoaderScoped(cas);
      }
    }.decorate(root);
  }

  private static Case classLoaderScoped(final Case cas) {
    return new Case(cas.name) {
      public void run() throws Throwable {
        Thread thread = Thread.currentThread();
        ClassLoader original = thread.getContextClassLoader();
        thread.setContextClassLoader(new ClassLoader(original) {});
        try {
          cas.run();
        } finally {
          thread.setContextClassLoader(original);
        }
      }
    };
  }
}
