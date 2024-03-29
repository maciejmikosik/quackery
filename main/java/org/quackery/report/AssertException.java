package org.quackery.report;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;

import java.util.Arrays;

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
      throw new AssertException(format("\n"
          + "  expected equal to\n"
          + "    %s\n"
          + "  but was\n"
          + "    %s\n",
          print(expected),
          print(actual)));
    }
  }

  private static String print(Object object) {
    if (object == null) {
      return "null";
    } else if (object.getClass().isArray()) {
      String string = Arrays.deepToString(new Object[] { object });
      return string.substring(1, string.length() - 1);
    } else {
      return object.toString();
    }
  }

  public static void fail() {
    throw new AssertException();
  }
}
