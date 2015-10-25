package org.quackery.run;

import org.quackery.Case;
import org.quackery.Test;

public class Runners {
  public static Test run(Test test) {
    return new Visitor() {
      protected Case visit(Case visiting) {
        return run(visiting);
      }
    }.visit(test);
  }

  private static Case run(Case visiting) {
    try {
      visiting.run();
    } catch (final Throwable throwable) {
      return new Case(visiting.name) {
        public void run() throws Throwable {
          throw throwable;
        }
      };
    }
    return new Case(visiting.name) {
      public void run() {}
    };
  }

  public static Test threadScoped(Test test) {
    return new Visitor() {
      protected Case visit(final Case visiting) {
        return threadScoped(visiting);
      }
    }.visit(test);
  }

  private static Case threadScoped(final Case visiting) {
    return new Case(visiting.name) {
      private Throwable throwable;

      public void run() throws Throwable {
        Thread thread = new Thread(new Runnable() {
          public void run() {
            try {
              visiting.run();
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

  public static Test classLoaderScoped(Test test) {
    return new Visitor() {
      protected Case visit(Case visiting) {
        return classLoaderScoped(visiting);
      }
    }.visit(test);
  }

  private static Case classLoaderScoped(final Case visiting) {
    return new Case(visiting.name) {
      public void run() throws Throwable {
        Thread thread = Thread.currentThread();
        ClassLoader original = thread.getContextClassLoader();
        thread.setContextClassLoader(new ClassLoader(original) {});
        try {
          visiting.run();
        } finally {
          thread.setContextClassLoader(original);
        }
      }
    };
  }
}
