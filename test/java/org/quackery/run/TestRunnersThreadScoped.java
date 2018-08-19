package org.quackery.run;

import static org.quackery.run.Runners.threadScoped;
import static org.quackery.run.TestingDecorators.decorator_preserves_case_result;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_runs_cases_lazily;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.assertNotEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.quackery.Case;
import org.quackery.Test;
import org.quackery.help.Decorator;

public class TestRunnersThreadScoped {
  public static void test_runners_thread_scoped() throws Throwable {
    Decorator decorator = new Decorator() {
      public Test decorate(Test test) {
        return threadScoped(test);
      }
    };

    decorator_preserves_names_and_structure(decorator);
    decorator_preserves_case_result(decorator);
    decorator_validates_arguments(decorator);
    decorator_runs_cases_lazily(decorator);

    runs_test_in_different_thread_than_caller();
    runs_each_test_in_different_thread();
  }

  private static void runs_test_in_different_thread_than_caller() throws Throwable {
    Thread callerThread = Thread.currentThread();
    final AtomicReference<Thread> scope = new AtomicReference<>();
    Test test = threadScoped(new Case("case") {
      public void run() {
        scope.set(Thread.currentThread());
      }
    });

    ((Case) test).run();

    assertNotEquals(scope.get(), null);
    assertNotEquals(scope.get(), callerThread);
  }

  private static void runs_each_test_in_different_thread() throws Throwable {
    final AtomicReference<Thread> scopeA = new AtomicReference<>();
    final AtomicReference<Thread> scopeB = new AtomicReference<>();
    Test testA = threadScoped(new Case("caseA") {
      public void run() {
        scopeA.set(Thread.currentThread());
      }
    });
    Test testB = threadScoped(new Case("caseB") {
      public void run() {
        scopeB.set(Thread.currentThread());
      }
    });

    ((Case) testA).run();
    ((Case) testB).run();

    assertNotEquals(scopeA.get(), null);
    assertNotEquals(scopeB.get(), null);
    assertNotEquals(scopeB.get(), scopeA.get());
  }
}
