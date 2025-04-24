package org.quackery.testing;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static org.quackery.Story.story;
import static org.quackery.testing.Type.STORY;
import static org.quackery.testing.Type.SUITE;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.quackery.Story;
import org.quackery.Suite;
import org.quackery.Test;

public class Testing {
  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }

  public static void assertEquals(Object actual, Object expected) {
    if (!deepEquals(actual, expected)) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected equal to\n"
          + "    %s\n"
          + "  but was\n"
          + "    %s\n",
          expected,
          actual));
    }
  }

  public static void assertNotEquals(Object actual, Object expected) {
    if (deepEquals(actual, expected)) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected not equal to\n"
          + "    %s\n"
          + "  but was\n"
          + "    %s\n",
          expected,
          actual));
    }
  }

  public static void assertChildren(Test actualTest, List<Test> expectedChildren) {
    switch (actualTest) {
      case Story story -> throw new AssertionError();
      case Suite suite -> {
        assertEquals(suite.children, expectedChildren);
      }
    }
  }

  public static Type typeOf(Test test) {
    return switch (test) {
      case Story story -> STORY;
      case Suite suite -> SUITE;
    };
  }

  public static String nameOf(Test test) {
    return switch (test) {
      case Story story -> story.name;
      case Suite suite -> suite.name;
    };
  }

  public static List<Test> childrenOf(Test test) {
    return switch (test) {
      case Story story -> throw new AssertionError();
      case Suite suite -> suite.children;
    };
  }

  public static void runAndThrow(Test test) throws Throwable {
    Optional<Throwable> thrown = runAndCatch(test);
    if (thrown.isPresent()) {
      throw thrown.get();
    }
  }

  public static Optional<Throwable> runAndCatch(Test test) {
    switch (test) {
      case Story story -> {
        try {
          story.script.run();
        } catch (Throwable throwable) {
          return Optional.of(throwable);
        }
        return Optional.empty();
      }
      case Suite suite -> throw new IllegalArgumentException();
    }
  }

  public static void fail() {
    throw new AssertionError();
  }

  public static Object mockObject(String name) {
    return new Object() {
      public boolean equals(Object object) {
        return object != null
            && getClass() == object.getClass()
            && toString().equals(object.toString());
      }

      public int hashCode() {
        return name.hashCode();
      }

      public String toString() {
        return name;
      }
    };
  }

  public static Test mockStory(String name) {
    return story(name, () -> {});
  }

  public static Test mockStory(String name, Throwable throwable) {
    return story(name, () -> {
      throw throwable.fillInStackTrace();
    });
  }

  public static void interruptMeAfter(double time) {
    Thread caller = Thread.currentThread();
    Thread interrupter = new Thread(new Runnable() {
      public void run() {
        try {
          sleep(time);
        } catch (InterruptedException e) {
          throw new Error(e);
        }
        caller.interrupt();
      }
    });
    interrupter.start();
  }

  public static Duration seconds(double seconds) {
    return Duration.ofNanos((long) (seconds * 1_000_000_000));
  }

  public static void sleep(double time) throws InterruptedException {
    Thread.sleep((long) (time * 1e3));
  }

  public static void sleepBusy(double time) {
    long nanos = (long) (time * 1e9);
    long start = System.nanoTime();
    while (System.nanoTime() - start < nanos) {}
  }
}
