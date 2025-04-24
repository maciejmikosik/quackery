package org.quackery.junit;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static org.quackery.Suite.suite;
import static org.quackery.testing.Testing.childrenOf;
import static org.quackery.testing.Testing.nameOf;
import static org.quackery.testing.Testing.runAndCatch;
import static org.quackery.testing.Testing.typeOf;
import static org.quackery.testing.Type.STORY;
import static org.quackery.testing.Type.SUITE;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.quackery.Story;
import org.quackery.Test;
import org.quackery.testing.Type;

public class TestingJunit {
  public static void assertResult(JunitClassBuilder junitClassBuilder, Test expected) {
    Runner runner = new QuackeryRunner(junitClassBuilder.load());
    Result result = new JUnitCore().run(runner);
    assertRecursively(
        runner.getDescription(),
        result.getFailures(),
        expected);
  }

  public static void assertResult(MethodDefinition methodDefinition, Test expected) {
    String name = "a.b.AnnotatedClass";
    assertResult(
        new JunitClassBuilder()
            .name(name)
            .define(methodDefinition),
        suite(name)
            .add(expected));
  }

  private static void assertRecursively(Description description, List<Failure> failures, Test expected) {
    if (!(deepEquals(nameOfJunit(description), nameOf(expected))
        && typeOfJunit(description) == typeOf(expected))) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected %s named\n"
          + "    %s\n"
          + "  but was %s named\n"
          + "    %s\n",
          typeOf(expected),
          nameOf(expected),
          typeOfJunit(description),
          nameOfJunit(description)));
    }
    if (description.isTest()) {
      Throwable actualThrowable = thrownBy(description, failures);
      Throwable expectedThrowable = thrownBy((Story) expected);
      if (actualThrowable.getClass() != expectedThrowable.getClass()
          || !deepEquals(actualThrowable.getMessage(), expectedThrowable.getMessage())) {
        throw new AssertionError(format(""
            + "\n"
            + "  expected Story named\n"
            + "    %s\n"
            + "  to throw\n"
            + "    %s\n"
            + "  but thrown\n"
            + "    %s\n",
            nameOf(expected),
            expectedThrowable,
            actualThrowable));
      }
    } else {
      List<Description> actualChildren = description.getChildren();
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
        assertRecursively(actualChildren.get(i), failures, expectedChildren.get(i));
      }
    }
  }

  private static String nameOfJunit(Description description) {
    return description.isSuite()
        ? description.getDisplayName()
        : description.getMethodName();
  }

  private static Type typeOfJunit(Description description) {
    return description.isSuite()
        ? SUITE
        : STORY;
  }

  private static Throwable thrownBy(Story test) {
    return runAndCatch(test)
        .orElse(new NoThrowable());
  }

  private static Throwable thrownBy(Description description, List<Failure> failures) {
    for (Failure failure : failures) {
      if (deepEquals(failure.getDescription(), description)) {
        return failure.getException();
      }
    }
    return new NoThrowable();
  }

  private static class NoThrowable extends Throwable {
    public String getMessage() {
      return "no throwable";
    }

    public String toString() {
      return "nothing";
    }
  }
}
