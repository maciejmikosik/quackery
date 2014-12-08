package org.testanza;

public class TestanzaException extends RuntimeException {
  public TestanzaException() {}

  public TestanzaException(String message) {
    super(message);
  }

  public TestanzaException(Throwable cause) {
    super(cause);
  }

  public TestanzaException(String message, Throwable cause) {
    super(message, cause);
  }

  public static void check(boolean condition) {
    if (!condition) {
      throw new TestanzaException();
    }
  }
}
