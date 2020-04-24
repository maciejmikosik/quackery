package org.quackery.junit;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static org.quackery.Suite.suite;

import java.util.Iterator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

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
    if (!(deepEquals(name(description), expected.name)
        && type(description) == type(expected))) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected %s named\n"
          + "    %s\n"
          + "  but was %s named\n"
          + "    %s\n",
          type(expected).getSimpleName(),
          expected.name,
          type(description).getSimpleName(),
          name(description)));
    }
    if (description.isTest()) {
      Throwable actualThrowable = thrownBy(description, failures);
      Throwable expectedThrowable = thrownBy((Case) expected);
      if (actualThrowable.getClass() != expectedThrowable.getClass()
          || !deepEquals(actualThrowable.getMessage(), expectedThrowable.getMessage())) {
        throw new AssertionError(format(""
            + "\n"
            + "  expected Case named\n"
            + "    %s\n"
            + "  to throw\n"
            + "    %s\n"
            + "  but thrown\n"
            + "    %s\n",
            expected.name,
            expectedThrowable,
            actualThrowable));
      }
    } else {
      List<Description> actualChildren = description.getChildren();
      List<Test> expectedChildren = ((Suite) expected).tests;
      if (actualChildren.size() != expectedChildren.size()) {
        throw new AssertionError(format(""
            + "\n"
            + "  expected that suite named\n"
            + "    %s\n"
            + "  number of children is\n"
            + "    %s\n"
            + "  but number of children was\n"
            + "    %s\n",
            expected.name,
            expectedChildren.size(),
            actualChildren.size()));
      }
      for (int i = 0; i < expectedChildren.size(); i++) {
        assertRecursively(actualChildren.get(i), failures, expectedChildren.get(i));
      }
    }
  }

  private static String name(Description description) {
    return description.isSuite()
        ? description.getDisplayName()
        : description.getMethodName();
  }

  // TODO use private enum CASE, SUITE instead of Class object
  private static Class<?> type(Test test) {
    return test.visit(
        (name, body) -> Case.class,
        (name, children) -> Suite.class);
  }

  private static Class<?> type(Description description) {
    return description.isSuite()
        ? Suite.class
        : Case.class;
  }

  private static Throwable thrownBy(Case test) {
    try {
      test.run();
    } catch (Throwable throwable) {
      return throwable;
    }
    return new NoThrowable();
  }

  private static Throwable thrownBy(Description description, List<Failure> failures) {
    Iterator<Failure> iterator = failures.iterator();
    while (iterator.hasNext()) {
      Failure failure = iterator.next();
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
