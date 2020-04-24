package org.quackery.run;

import static org.quackery.run.Runners.expect;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_runs_cases_lazily;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.fail;

import java.io.IOException;
import java.util.function.Function;

import org.quackery.Case;
import org.quackery.Test;
import org.quackery.report.AssertException;

public class TestRunnersExpect {
  public static void test_runners_expect() throws Throwable {
    Function<Test, Test> decorator = test -> expect(Throwable.class, test);

    decorator_preserves_names_and_structure(decorator);
    decorator_validates_arguments(decorator);
    decorator_runs_cases_lazily(decorator);

    case_succeeds_if_thrown_expected_throwable();
    case_succeeds_if_thrown_subtype_of_expected_throwable();
    case_fails_if_thrown_supertype_of_expected_throwable();
    case_fails_if_thrown_nothing();
  }

  private static void case_succeeds_if_thrown_expected_throwable() throws Throwable {
    Test test = Runners.expect(SuperException.class, new Case("case") {
      public void run() {
        throw new SuperException();
      }
    });
    ((Case) test).run();
  }

  private static void case_succeeds_if_thrown_subtype_of_expected_throwable() throws Throwable {
    Test test = Runners.expect(SuperException.class, new Case("case") {
      public void run() {
        throw new SubException();
      }
    });
    ((Case) test).run();
  }

  private static void case_fails_if_thrown_supertype_of_expected_throwable() throws Throwable {
    final SuperException thrown = new SuperException();
    Test test = Runners.expect(SubException.class, new Case("case") {
      public void run() {
        throw thrown;
      }
    });
    try {
      ((Case) test).run();
      fail();
    } catch (AssertException e) {
      assertEquals(e.getCause(), thrown);
    }
  }

  private static void case_fails_if_thrown_nothing() throws Throwable {
    Test test = Runners.expect(IOException.class, new Case("case") {
      public void run() {}
    });
    try {
      ((Case) test).run();
      fail();
    } catch (AssertException e) {
      assertEquals(e.getMessage(), "nothing thrown");
    }
  }

  private static class SuperException extends RuntimeException {}

  private static class SubException extends SuperException {}
}
