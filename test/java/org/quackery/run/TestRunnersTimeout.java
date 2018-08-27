package org.quackery.run;

import static org.quackery.report.AssertException.assertTrue;
import static org.quackery.run.Runners.timeout;
import static org.quackery.run.TestingDecorators.decorator_preserves_case_result;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_runs_cases_lazily;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;
import static org.quackery.testing.Testing.sleep;
import static org.quackery.testing.Testing.sleepBusy;

import java.util.concurrent.atomic.AtomicBoolean;

import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Test;
import org.quackery.help.Decorator;

public class TestRunnersTimeout {
  public static void test_runners_timeout() throws Throwable {
    Decorator decorator = new Decorator() {
      public Test decorate(Test test) {
        return timeout(1, test);
      }
    };

    decorator_preserves_names_and_structure(decorator);
    decorator_preserves_case_result(decorator);
    decorator_validates_arguments(decorator);
    decorator_runs_cases_lazily(decorator);

    interrupts_interruptible_case();
    interrupts_uninterruptible_successful_case();
    interrupts_uninterruptible_failing_case();
    validates_arguments();
  }

  private static void interrupts_interruptible_case() throws Throwable {
    final AtomicBoolean interrupted = new AtomicBoolean(false);

    Test test = timeout(0.01, new Case("case") {
      public void run() throws InterruptedException {
        try {
          sleep(0.02);
          interrupted.set(false);
        } catch (InterruptedException e) {
          interrupted.set(true);
          throw e;
        }
      }
    });

    try {
      ((Case) test).run();
      fail();
    } catch (InterruptedException e) {}
    assertTrue(interrupted.get());
  }

  private static void interrupts_uninterruptible_successful_case() throws Throwable {
    Test test = timeout(0.01, new Case("case") {
      public void run() {
        sleepBusy(0.02);
      }
    });

    try {
      ((Case) test).run();
      fail();
    } catch (InterruptedException e) {}
  }

  private static void interrupts_uninterruptible_failing_case() throws Throwable {
    Test test = timeout(0.01, new Case("case") {
      public void run() {
        sleepBusy(0.02);
        throw new RuntimeException();
      }
    });

    try {
      ((Case) test).run();
      fail();
    } catch (InterruptedException e) {}
  }

  private static void validates_arguments() {
    Case test = mockCase("case");
    try {
      timeout(-0.001, test);
      fail();
    } catch (QuackeryException e) {}
  }
}
