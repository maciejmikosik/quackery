package org.quackery.run;

import static org.quackery.Case.newCase;
import static org.quackery.report.AssertException.assertTrue;
import static org.quackery.run.Runners.timeout;
import static org.quackery.run.TestingDecorators.decorator_preserves_case_result;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_runs_cases_lazily;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;
import static org.quackery.testing.Testing.runAndThrow;
import static org.quackery.testing.Testing.sleep;
import static org.quackery.testing.Testing.sleepBusy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestRunnersTimeout {
  public static void test_runners_timeout() throws Throwable {
    Function<Test, Test> decorator = test -> timeout(1, test);

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
    AtomicBoolean interrupted = new AtomicBoolean(false);
    Test test = timeout(0.01, newCase("case", () -> {
      try {
        sleep(0.02);
        interrupted.set(false);
      } catch (InterruptedException e) {
        interrupted.set(true);
        throw e;
      }
    }));

    try {
      runAndThrow(test);
      fail();
    } catch (InterruptedException e) {}
    assertTrue(interrupted.get());
  }

  private static void interrupts_uninterruptible_successful_case() throws Throwable {
    Test test = timeout(0.01, newCase("case", () -> {
      sleepBusy(0.02);
    }));

    try {
      runAndThrow(test);
      fail();
    } catch (InterruptedException e) {}
  }

  private static void interrupts_uninterruptible_failing_case() throws Throwable {
    Test test = timeout(0.01, newCase("case", () -> {
      sleepBusy(0.02);
      throw new RuntimeException();
    }));

    try {
      runAndThrow(test);
      fail();
    } catch (InterruptedException e) {}
  }

  private static void validates_arguments() {
    Test test = mockCase("case");
    try {
      timeout(-0.001, test);
      fail();
    } catch (QuackeryException e) {}
  }
}
