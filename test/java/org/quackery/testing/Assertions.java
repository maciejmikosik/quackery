package org.quackery.testing;

import static org.quackery.testing.Tests.runQuietly;

import java.util.Objects;

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
    Report report = runQuietly(test);
    boolean expected = report.problems.size() == 0;
    if (!expected) {
      throw new AssertionError("expected success: " + test.name + report);
    }
  }

  public static void assertFailure(Test test) {
    Report report = runQuietly(test);
    boolean expected = report.failures().size() > 0 && report.errors().size() == 0;
    if (!expected) {
      throw new AssertionError("expected failure: " + test.name + report);
    }
  }
}
