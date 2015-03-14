package org.testanza;

public class TestanzaAssumptionException extends RuntimeException {
  public TestanzaAssumptionException() {}

  public TestanzaAssumptionException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public TestanzaAssumptionException(String message, Throwable cause) {
    super(message, cause);
  }

  public TestanzaAssumptionException(String message) {
    super(message);
  }

  public TestanzaAssumptionException(Throwable cause) {
    super(cause);
  }

  public static void check(boolean condition) {
    if (!condition) {
      throw new TestanzaAssumptionException();
    }
  }
}
