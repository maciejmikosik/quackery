package org.quackery.run;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.quackery.Suite.suite;
import static org.quackery.run.Runners.runIn;
import static org.quackery.testing.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.quackery.Case;
import org.quackery.Test;

public class test_Runners_runIn extends test_Runner {
  private Test test;
  private ExecutorService executor;

  protected Test visit(Test visiting) {
    return runIn(currentThreadExecutor(), visiting);
  }

  public void submits_asynchronously_to_executor() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(3);
    executor = newCachedThreadPool();
    test = suite("")
        .test(countDown(latch))
        .test(countDown(latch))
        .test(countDown(latch));

    // when
    runIn(executor, test);

    // then no deadlock

    // tear down
    executor.shutdown();
    assertTrue(executor.awaitTermination(1, SECONDS));
  }

  public void runs_test_eagerly() throws InterruptedException {
    executor = newCachedThreadPool();
    test = suite("")
        .test(sleepFor(100, MILLISECONDS))
        .test(sleepFor(100, MILLISECONDS))
        .test(sleepFor(100, MILLISECONDS));

    // when
    runIn(executor, test);

    // then
    executor.shutdown();
    assertTrue(executor.awaitTermination(1, MILLISECONDS));
  }

  private static Case countDown(final CountDownLatch latch) {
    return new Case("") {
      public void run() throws Throwable {
        latch.countDown();
        latch.await();
      }
    };
  }

  private static Case sleepFor(final long timeout, final TimeUnit unit) {
    return new Case("") {
      public void run() throws Throwable {
        unit.sleep(timeout);
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
