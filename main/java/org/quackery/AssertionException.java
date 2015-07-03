package org.quackery;

import static java.util.Objects.deepEquals;

public class AssertionException extends RuntimeException {
  public AssertionException() {}

  public AssertionException(String message) {
    super(message);
  }

  public AssertionException(String message, Throwable cause) {
    super(message, cause);
  }

  public AssertionException(Throwable cause) {
    super(cause);
  }

  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionException();
    }
  }

  public static void assertEquals(Object actual, Object expected) {
    if (!deepEquals(actual, expected)) {
      throw new AssertionException(""
          + "\n"
          + "  expected that\n"
          + "    " + actual + "\n"
          + "  is equal to\n"
          + "    " + expected + "\n");
    }
  }

  public static void fail() {
    throw new AssertionException();
  }
}
