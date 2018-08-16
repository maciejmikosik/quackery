package org.quackery.run;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.quackery.Suite.suite;
import static org.quackery.run.Runners.runIn;
import static org.quackery.run.TestingVisitors.visitor_preserves_case_result;
import static org.quackery.run.TestingVisitors.visitor_preserves_names_and_structure;
import static org.quackery.run.TestingVisitors.visitor_runs_cases;
import static org.quackery.run.TestingVisitors.visitor_validates_arguments;
import static org.quackery.testing.Testing.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.quackery.Case;
import org.quackery.Test;

public class TestRunnersRunIn {
  public static void test_runners_run_in() throws Throwable {
    Visitor visitor = new Visitor() {
      public Test visit(Test visiting) {
        return runIn(currentThreadExecutor(), visiting);
      }
    };

    visitor_preserves_names_and_structure(visitor);
    visitor_preserves_case_result(visitor);
    visitor_validates_arguments(visitor);
    visitor_runs_cases(visitor);

    submits_asynchronously_to_executor();
  }

  private static void submits_asynchronously_to_executor() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(3);
    ExecutorService executor = newCachedThreadPool();
    AtomicBoolean failed = new AtomicBoolean(false);
    Test test = suite("")
        .add(countDown(latch, failed))
        .add(countDown(latch, failed))
        .add(countDown(latch, failed));

    runIn(executor, test);

    assertTrue(!failed.get());

    executor.shutdown();
    assertTrue(executor.awaitTermination(1, SECONDS));
  }

  private static Case countDown(final CountDownLatch latch, final AtomicBoolean failed) {
    return new Case("countDown") {
      public void run() throws InterruptedException {
        latch.countDown();
        if (!latch.await(1, SECONDS)) {
          failed.set(true);
        }
      }
    };
  }

  private static Executor currentThreadExecutor() {
    return new Executor() {
      public void execute(Runnable runnable) {
        runnable.run();
      }
    };
  }
}
