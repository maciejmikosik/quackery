package org.quackery.run;

import static org.quackery.Story.story;
import static org.quackery.run.Runners.threadScoped;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_preserves_story_result;
import static org.quackery.run.TestingDecorators.decorator_runs_story_lazily;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.assertNotEquals;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.interruptMeAfter;
import static org.quackery.testing.Testing.runAndThrow;
import static org.quackery.testing.Testing.sleep;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.quackery.Test;

public class TestRunnersThreadScoped {
  public static void test_runners_thread_scoped() throws Throwable {
    Function<Test, Test> decorator = test -> threadScoped(test);

    decorator_preserves_names_and_structure(decorator);
    decorator_preserves_story_result(decorator);
    decorator_validates_arguments(decorator);
    decorator_runs_story_lazily(decorator);

    propagates_interruption();
    runs_test_in_different_thread_than_caller();
    runs_each_test_in_different_thread();
  }

  private static void runs_test_in_different_thread_than_caller() throws Throwable {
    Thread callerThread = Thread.currentThread();
    AtomicReference<Thread> scope = new AtomicReference<>();
    Test test = threadScoped(story("story", () -> {
      scope.set(Thread.currentThread());
    }));

    runAndThrow(test);

    assertNotEquals(scope.get(), null);
    assertNotEquals(scope.get(), callerThread);
  }

  private static void runs_each_test_in_different_thread() throws Throwable {
    AtomicReference<Thread> scopeA = new AtomicReference<>();
    AtomicReference<Thread> scopeB = new AtomicReference<>();
    Test testA = threadScoped(story("storyA", () -> {
      scopeA.set(Thread.currentThread());
    }));
    Test testB = threadScoped(story("storyB", () -> {
      scopeB.set(Thread.currentThread());
    }));

    runAndThrow(testA);
    runAndThrow(testB);

    assertNotEquals(scopeA.get(), null);
    assertNotEquals(scopeB.get(), null);
    assertNotEquals(scopeB.get(), scopeA.get());
  }

  private static void propagates_interruption() throws Throwable {
    AtomicBoolean interrupted = new AtomicBoolean(false);
    Test test = threadScoped(story("story", () -> {
      try {
        sleep(1);
        interrupted.set(false);
      } catch (InterruptedException e) {
        interrupted.set(true);
        throw e;
      }
    }));

    interruptMeAfter(0.01);
    try {
      runAndThrow(test);
      fail();
    } catch (InterruptedException e) {}
    assertTrue(interrupted.get());
  }
}
