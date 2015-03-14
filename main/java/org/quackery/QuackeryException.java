package org.quackery;

public class QuackeryException extends RuntimeException {
  public QuackeryException() {}

  public QuackeryException(String message) {
    super(message);
  }

  public QuackeryException(Throwable cause) {
    super(cause);
  }

  public QuackeryException(String message, Throwable cause) {
    super(message, cause);
  }

  public static void check(boolean condition) {
    if (!condition) {
      throw new QuackeryException();
    }
  }
}
