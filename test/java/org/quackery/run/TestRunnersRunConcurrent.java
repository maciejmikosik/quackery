package org.quackery.run;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.quackery.Case.newCase;
import static org.quackery.Suite.suite;
import static org.quackery.run.Runners.concurrent;
import static org.quackery.run.Runners.run;
import static org.quackery.run.TestingDecorators.decorator_preserves_case_result;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_runs_cases_eagerly;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;
import static org.quackery.testing.Testing.runAndThrow;
import static org.quackery.testing.Testing.sleep;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestRunnersRunConcurrent {
  public static void test_runners_run_concurrent() throws Throwable {
    Function<Test, Test> decorator = test -> run(concurrent(test));

    decorator_preserves_names_and_structure(decorator);
    decorator_preserves_case_result(decorator);
    decorator_validates_arguments(decorator);
    decorator_runs_cases_eagerly(decorator);

    shuts_down_executor();
    runs_concurrently();
    validates_arguments();
  }

  private static void shuts_down_executor() throws Throwable {
    runAndThrow(concurrent(mockCase("case")));
    sleep(0.01);
    assertTrue(Thread.currentThread().getThreadGroup().activeCount() == 1);
  }

  private static void runs_concurrently() {
    assertTrue(Runtime.getRuntime().availableProcessors() >= 3);

    CountDownLatch latch = new CountDownLatch(3);
    AtomicBoolean failed = new AtomicBoolean(false);
    Test test = suite("")
        .add(countDown(latch, failed))
        .add(countDown(latch, failed))
        .add(countDown(latch, failed));

    run(concurrent(test));

    assertTrue(!failed.get());
  }

  private static Test countDown(CountDownLatch latch, AtomicBoolean failed) {
    return newCase("countDown", () -> {
      latch.countDown();
      if (!latch.await(1, SECONDS)) {
        failed.set(true);
      }
    });
  }

  private static void validates_arguments() {
    try {
      concurrent(null);
      fail();
    } catch (QuackeryException e) {}
  }
}
