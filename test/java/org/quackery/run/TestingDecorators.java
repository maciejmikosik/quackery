package org.quackery.run;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static org.quackery.Story.story;
import static org.quackery.Suite.suite;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.childrenOf;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockStory;
import static org.quackery.testing.Testing.nameOf;
import static org.quackery.testing.Testing.runAndThrow;
import static org.quackery.testing.Testing.typeOf;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;

public class TestingDecorators {
  public static void decorator_preserves_names_and_structure(Function<Test, Test> decorator) {
    decorator_preserves_names_and_structure(decorator, mockStory("story"));
    decorator_preserves_names_and_structure(decorator, mockStory("story", new Throwable()));
    decorator_preserves_names_and_structure(decorator, suite("suite").add(mockStory("story")));
    decorator_preserves_names_and_structure(decorator, suite("suite"));
    decorator_preserves_names_and_structure(decorator, suite("suiteA")
        .add(suite("suiteB")
            .add(mockStory("storyA"))
            .add(mockStory("storyB")))
        .add(suite("suiteC")
            .add(mockStory("storyC"))
            .add(mockStory("storyD"))));
  }

  private static void decorator_preserves_names_and_structure(Function<Test, Test> decorator, Test test) {
    assertEqualNamesAndStructure(decorator.apply(test), test);
  }

  private static void assertEqualNamesAndStructure(Test actual, Test expected) {
    if (!deepEquals(nameOf(actual), nameOf(expected))
        || typeOf(actual) != typeOf(expected)) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected %s named\n"
          + "    %s\n"
          + "  but was %s named\n"
          + "    %s\n",
          typeOf(expected),
          nameOf(expected),
          typeOf(actual),
          nameOf(actual)));
    }
    if (actual instanceof Suite) {
      List<Test> actualChildren = childrenOf(actual);
      List<Test> expectedChildren = childrenOf(expected);
      if (actualChildren.size() != expectedChildren.size()) {
        throw new AssertionError(format(""
            + "\n"
            + "  expected that suite named\n"
            + "    %s\n"
            + "  number of children is\n"
            + "    %s\n"
            + "  but number of children was\n"
            + "    %s\n",
            nameOf(expected),
            expectedChildren.size(),
            actualChildren.size()));
      }
      for (int i = 0; i < expectedChildren.size(); i++) {
        assertEqualNamesAndStructure(actualChildren.get(i), expectedChildren.get(i));
      }
    }
  }

  public static void decorator_preserves_story_result(Function<Test, Test> decorator) throws Throwable {
    decorator_preserves_story_result_if_successful(decorator);
    decorator_preserves_story_result_if_failed(decorator);
  }

  private static void decorator_preserves_story_result_if_successful(Function<Test, Test> decorator) throws Throwable {
    Test test = decorator.apply(mockStory("name"));
    runAndThrow(test);
  }

  private static void decorator_preserves_story_result_if_failed(Function<Test, Test> decorator) {
    Throwable throwable = new Throwable();
    Test test = decorator.apply(mockStory("name", throwable));

    try {
      runAndThrow(test);
      fail();
    } catch (Throwable t) {
      assertEquals(t, throwable);
    }
  }

  public static void decorator_validates_arguments(Function<Test, Test> decorator) {
    try {
      decorator.apply((Test) null);
      fail();
    } catch (QuackeryException e) {}
  }

  public static void decorator_runs_story_eagerly(Function<Test, Test> decorator) {
    decorator_runs_story(1, decorator);
    decorator_runs_successful_decorated(0, decorator);
    decorator_runs_failed_decorated(0, decorator);
  }

  public static void decorator_runs_story_lazily(Function<Test, Test> decorator) {
    decorator_runs_story(0, decorator);
    decorator_runs_successful_decorated(1, decorator);
    decorator_runs_failed_decorated(1, decorator);
  }

  private static void decorator_runs_story(int count, Function<Test, Test> decorator) {
    AtomicInteger invoked = new AtomicInteger();
    Test test = story("name", () -> {
      invoked.incrementAndGet();
    });

    decorator.apply(test);

    assertEquals(invoked.get(), count);
  }

  private static void decorator_runs_successful_decorated(int count, Function<Test, Test> decorator) {
    AtomicInteger invoked = new AtomicInteger();
    Test test = story("name", () -> {
      invoked.incrementAndGet();
    });
    Test decorated = decorator.apply(test);
    invoked.set(0);

    try {
      runAndThrow(decorated);
    } catch (Throwable t) {}

    assertEquals(invoked.get(), count);
  }

  private static void decorator_runs_failed_decorated(int count, Function<Test, Test> decorator) {
    AtomicInteger invoked = new AtomicInteger();
    Test test = story("name", () -> {
      invoked.incrementAndGet();
      throw new RuntimeException();
    });
    Test decorated = decorator.apply(test);
    invoked.set(0);

    try {
      runAndThrow(decorated);
    } catch (Throwable t) {}

    assertEquals(invoked.get(), count);
  }
}
