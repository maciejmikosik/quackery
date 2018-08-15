package org.quackery.run;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.quackery.Suite.suite;
import static org.quackery.run.Runners.runIn;
import static org.quackery.testing.Testing.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.quackery.Case;
import org.quackery.Test;

public class TestRunnersRunIn extends TestRunner {
  private Test test;
  private ExecutorService executor;
  private boolean failed = false;

  protected Test visit(Test visiting) {
    return runIn(currentThreadExecutor(), visiting);
  }

  public void submits_asynchronously_to_executor() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(3);
    executor = newCachedThreadPool();
    test = suite("")
        .add(countDown(latch))
        .add(countDown(latch))
        .add(countDown(latch));

    // when
    runIn(executor, test);

    // then
    assertTrue(!failed);

    // tear down
    executor.shutdown();
    assertTrue(executor.awaitTermination(1, SECONDS));
  }

  private Case countDown(final CountDownLatch latch) {
    return new Case("") {
      public void run() throws InterruptedException {
        latch.countDown();
        if (!latch.await(1, SECONDS)) {
          failed = true;
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
