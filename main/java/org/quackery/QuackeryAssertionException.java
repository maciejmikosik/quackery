package org.quackery;

public class QuackeryAssertionException extends RuntimeException {
  public QuackeryAssertionException() {}

  public QuackeryAssertionException(String message) {
    super(message);
  }

  public QuackeryAssertionException(String message, Throwable cause) {
    super(message, cause);
  }
}
