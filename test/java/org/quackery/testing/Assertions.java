package org.quackery.testing;

import static org.quackery.run.Reports.count;
import static org.quackery.run.Reports.print;
import static org.quackery.run.Runners.run;

import java.util.Objects;

import org.quackery.AssertionException;
import org.quackery.AssumptionException;
import org.quackery.Test;

public class Assertions {
  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }

  public static void assertEquals(Object actual, Object expected) {
    if (!Objects.deepEquals(actual, expected)) {
      throw new AssertionError(""
          + "\n"
          + "  expected that\n"
          + "    " + actual + "\n"
          + "  is equal to\n"
          + "    " + expected + "\n");
    }
  }

  public static void assertNotEquals(Object actual, Object expected) {
    if (Objects.deepEquals(actual, expected)) {
      throw new AssertionError(""
          + "\n"
          + "  expected that\n"
          + "    " + actual + "\n"
          + "  is not equal to\n"
          + "    " + expected + "\n");
    }
  }

  public static void fail() {
    throw new AssertionError();
  }

  public static void assertSuccess(Test test) {
    Test report = run(test);
    int problems = count(Throwable.class, report);
    boolean expected = problems == 0;
    if (!expected) {
      throw new AssertionError("expected success but was\n\n" + print(report));
    }
  }

  public static void assertFailure(Test test) {
    Test report = run(test);

    int failures = count(AssertionException.class, report);
    int misassumptions = count(AssumptionException.class, report);
    int problems = count(Throwable.class, report);
    int errors = problems - failures - misassumptions;

    boolean expected = failures > 0 && errors == 0;
    if (!expected) {
      throw new AssertionError("expected failure but was\n\n" + print(report));
    }
  }
}
