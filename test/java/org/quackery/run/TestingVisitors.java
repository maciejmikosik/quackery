package org.quackery.run;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static org.quackery.Suite.suite;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;

public class TestingVisitors {
  public static void visitor_preserves_names_and_structure(Visitor visitor) throws Throwable {
    visitor_preserves_names_and_structure(visitor, mockCase("case"));
    visitor_preserves_names_and_structure(visitor, mockCase("case", new Throwable()));
    visitor_preserves_names_and_structure(visitor, suite("suite").add(mockCase("case")));
    visitor_preserves_names_and_structure(visitor, suite("suite"));
    visitor_preserves_names_and_structure(visitor, suite("suiteA")
        .add(suite("suiteB")
            .add(mockCase("caseA"))
            .add(mockCase("caseB")))
        .add(suite("suiteC")
            .add(mockCase("caseC"))
            .add(mockCase("caseD"))));
  }

  private static void visitor_preserves_names_and_structure(Visitor visitor, Test test) {
    assertEqualNamesAndStructure(visitor.visit(test), test);
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

  private static Class<?> type(Test test) {
    return test instanceof Suite
        ? Suite.class
        : Case.class;
  }

  public static void visitor_preserves_case_result(Visitor visitor) throws Throwable {
    visitor_preserves_case_result_if_successful(visitor);
    visitor_preserves_case_result_if_failed(visitor);
  }

  private static void visitor_preserves_case_result_if_successful(Visitor visitor) throws Throwable {
    Test test = visitor.visit(mockCase("name"));
    ((Case) test).run();
  }

  private static void visitor_preserves_case_result_if_failed(Visitor visitor) {
    Throwable throwable = new Throwable();
    Test test = visitor.visit(mockCase("name", throwable));

    try {
      ((Case) test).run();
      fail();
    } catch (Throwable t) {
      assertEquals(t, throwable);
    }
  }

  public static void visitor_validates_arguments(Visitor visitor) {
    try {
      visitor.visit((Test) null);
      fail();
    } catch (QuackeryException e) {}
  }

  public static void visitor_runs_cases(Visitor visitor) throws Throwable {
    visitor_runs_case_once(visitor);
    visitor_report_does_not_run_successful_case(visitor);
    visitor_report_does_not_run_failed_case(visitor);
  }

  private static void visitor_runs_case_once(Visitor visitor) {
    final AtomicInteger invoked = new AtomicInteger();
    Test test = new Case("name") {
      public void run() {
        invoked.incrementAndGet();
      }
    };

    visitor.visit(test);

    assertEquals(invoked.get(), 1);
  }

  private static void visitor_report_does_not_run_successful_case(Visitor visitor) throws Throwable {
    final AtomicInteger invoked = new AtomicInteger();
    Test test = new Case("name") {
      public void run() {
        invoked.incrementAndGet();
      }
    };
    Test report = visitor.visit(test);
    invoked.set(0);

    ((Case) report).run();

    assertEquals(invoked.get(), 0);
  }

  private static void visitor_report_does_not_run_failed_case(Visitor visitor) {
    final AtomicInteger invoked = new AtomicInteger();
    Test test = new Case("name") {
      public void run() {
        invoked.incrementAndGet();
        throw new RuntimeException();
      }
    };
    Test report = visitor.visit(test);
    invoked.set(0);

    try {
      ((Case) report).run();
    } catch (Throwable t) {}

    assertEquals(invoked.get(), 0);
  }
}
