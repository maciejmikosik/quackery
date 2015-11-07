package org.quackery.report;

import static java.util.Objects.deepEquals;

public class AssertException extends ReportException {
  public AssertException() {}

  public AssertException(String message) {
    super(message);
  }

  public AssertException(String message, Throwable cause) {
    super(message, cause);
  }

  public AssertException(Throwable cause) {
    super(cause);
  }

  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertException();
    }
  }

  public static void assertEquals(Object actual, Object expected) {
    if (!deepEquals(actual, expected)) {
      throw new AssertException(""
          + "\n"
          + "  expected that\n"
          + "    " + actual + "\n"
          + "  is equal to\n"
          + "    " + expected + "\n");
    }
  }

  public static void fail() {
    throw new AssertException();
  }
}
