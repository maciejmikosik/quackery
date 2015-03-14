package org.quackery;

public class QuackeryAssumptionException extends RuntimeException {
  public QuackeryAssumptionException() {}

  public QuackeryAssumptionException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public QuackeryAssumptionException(String message, Throwable cause) {
    super(message, cause);
  }

  public QuackeryAssumptionException(String message) {
    super(message);
  }

  public QuackeryAssumptionException(Throwable cause) {
    super(cause);
  }

  public static void check(boolean condition) {
    if (!condition) {
      throw new QuackeryAssumptionException();
    }
  }
}
