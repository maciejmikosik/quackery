package org.quackery;

import static java.util.Objects.deepEquals;

public class FailureException extends RuntimeException {
  public FailureException() {}

  public FailureException(String message) {
    super(message);
  }

  public FailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public FailureException(Throwable cause) {
    super(cause);
  }

  public static void assertThat(boolean condition) {
    if (!condition) {
      throw new FailureException();
    }
  }

  public static void assertEquals(Object a, Object b) {
    if (!deepEquals(a, b)) {
      throw new FailureException();
    }
  }

  public static void fail() {
    throw new FailureException();
  }
}
