package org.quackery.run;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static org.quackery.Case.newCase;
import static org.quackery.Suite.suite;
import static org.quackery.help.Helpers.type;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;

public class TestingDecorators {
  public static void decorator_preserves_names_and_structure(Function<Test, Test> decorator) throws Throwable {
    decorator_preserves_names_and_structure(decorator, mockCase("case"));
    decorator_preserves_names_and_structure(decorator, mockCase("case", new Throwable()));
    decorator_preserves_names_and_structure(decorator, suite("suite").add(mockCase("case")));
    decorator_preserves_names_and_structure(decorator, suite("suite"));
    decorator_preserves_names_and_structure(decorator, suite("suiteA")
        .add(suite("suiteB")
            .add(mockCase("caseA"))
            .add(mockCase("caseB")))
        .add(suite("suiteC")
            .add(mockCase("caseC"))
            .add(mockCase("caseD"))));
  }

  private static void decorator_preserves_names_and_structure(Function<Test, Test> decorator, Test test) {
    assertEqualNamesAndStructure(decorator.apply(test), test);
  }

  private static void assertEqualNamesAndStructure(Test actual, Test expected) {
    if (!deepEquals(actual.name, expected.name)
        || type(actual) != type(expected)) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected %s named\n"
          + "    %s\n"
          + "  but was %s named\n"
          + "    %s\n",
          type(expected).getSimpleName(),
          expected.name,
          type(actual).getSimpleName(),
          actual.name));
    }
    if (actual instanceof Suite) {
      List<Test> actualChildren = ((Suite) actual).tests;
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
        assertEqualNamesAndStructure(actualChildren.get(i), expectedChildren.get(i));
      }
    }
  }

  public static void decorator_preserves_case_result(Function<Test, Test> decorator) throws Throwable {
    decorator_preserves_case_result_if_successful(decorator);
    decorator_preserves_case_result_if_failed(decorator);
  }

  private static void decorator_preserves_case_result_if_successful(Function<Test, Test> decorator) throws Throwable {
    Test test = decorator.apply(mockCase("name"));
    ((Case) test).run();
  }

  private static void decorator_preserves_case_result_if_failed(Function<Test, Test> decorator) {
    Throwable throwable = new Throwable();
    Test test = decorator.apply(mockCase("name", throwable));

    try {
      ((Case) test).run();
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

  public static void decorator_runs_cases_eagerly(Function<Test, Test> decorator) throws Throwable {
    decorator_runs_case(1, decorator);
    decorator_runs_successful_decorated(0, decorator);
    decorator_runs_failed_decorated(0, decorator);
  }

  public static void decorator_runs_cases_lazily(Function<Test, Test> decorator) throws Throwable {
    decorator_runs_case(0, decorator);
    decorator_runs_successful_decorated(1, decorator);
    decorator_runs_failed_decorated(1, decorator);
  }

  private static void decorator_runs_case(int count, Function<Test, Test> decorator) {
    AtomicInteger invoked = new AtomicInteger();
    Test test = newCase("name", () -> {
      invoked.incrementAndGet();
    });

    decorator.apply(test);

    assertEquals(invoked.get(), count);
  }

  private static void decorator_runs_successful_decorated(int count, Function<Test, Test> decorator) throws Throwable {
    AtomicInteger invoked = new AtomicInteger();
    Test test = newCase("name", () -> {
      invoked.incrementAndGet();
    });
    Test decorated = decorator.apply(test);
    invoked.set(0);

    try {
      ((Case) decorated).run();
    } catch (Throwable t) {}

    assertEquals(invoked.get(), count);
  }

  private static void decorator_runs_failed_decorated(int count, Function<Test, Test> decorator) {
    AtomicInteger invoked = new AtomicInteger();
    Test test = newCase("name", () -> {
      invoked.incrementAndGet();
      throw new RuntimeException();
    });
    Test decorated = decorator.apply(test);
    invoked.set(0);

    try {
      ((Case) decorated).run();
    } catch (Throwable t) {}

    assertEquals(invoked.get(), count);
  }
}
