package org.quackery;

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
}
