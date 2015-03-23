package org.quackery;

public class AssumptionException extends RuntimeException {
  public AssumptionException() {}

  public AssumptionException(String message) {
    super(message);
  }

  public AssumptionException(String message, Throwable cause) {
    super(message, cause);
  }

  public AssumptionException(Throwable cause) {
    super(cause);
  }

  public static void assume(boolean condition) {
    if (!condition) {
      throw new AssumptionException();
    }
  }
}
