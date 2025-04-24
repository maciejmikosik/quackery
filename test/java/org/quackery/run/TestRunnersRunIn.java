package org.quackery.run;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.quackery.Story.story;
import static org.quackery.Suite.suite;
import static org.quackery.run.Runners.in;
import static org.quackery.run.Runners.run;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_preserves_story_result;
import static org.quackery.run.TestingDecorators.decorator_runs_story_eagerly;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockStory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestRunnersRunIn {
  public static void test_runners_run_in() throws Throwable {
    Function<Test, Test> decorator = test -> run(in(currentThreadExecutor(), test));

    decorator_preserves_names_and_structure(decorator);
    decorator_preserves_story_result(decorator);
    decorator_validates_arguments(decorator);
    decorator_runs_story_eagerly(decorator);

    submits_asynchronously_to_executor();
    validates_arguments();
  }

  private static void submits_asynchronously_to_executor() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(3);
    ExecutorService executor = newCachedThreadPool();
    AtomicBoolean failed = new AtomicBoolean(false);
    Test test = suite("")
        .add(countDown(latch, failed))
        .add(countDown(latch, failed))
        .add(countDown(latch, failed));

    run(in(executor, test));

    assertTrue(!failed.get());

    executor.shutdown();
    assertTrue(executor.awaitTermination(1, SECONDS));
  }

  private static Test countDown(CountDownLatch latch, AtomicBoolean failed) {
    return story("countDown", () -> {
      latch.countDown();
      if (!latch.await(1, SECONDS)) {
        failed.set(true);
      }
    });
  }

  private static void validates_arguments() {
    try {
      in(null, mockStory("story"));
      fail();
    } catch (QuackeryException e) {}
    try {
      in(currentThreadExecutor(), null);
      fail();
    } catch (QuackeryException e) {}
  }

  private static Executor currentThreadExecutor() {
    return new Executor() {
      public void execute(Runnable runnable) {
        runnable.run();
      }
    };
  }
}
