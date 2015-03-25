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

  public static void assertThat(boolean condition) {
    if (!condition) {
      throw new AssertionException();
    }
  }

  public static void assertEquals(Object a, Object b) {
    if (!deepEquals(a, b)) {
      throw new AssertionException();
    }
  }

  public static void fail() {
    throw new AssertionException();
  }
}
