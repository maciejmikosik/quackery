package org.quackery.report;

public class AssumeException extends RuntimeException {
  public AssumeException() {}

  public AssumeException(String message) {
    super(message);
  }

  public AssumeException(String message, Throwable cause) {
    super(message, cause);
  }

  public AssumeException(Throwable cause) {
    super(cause);
  }

  public static void assume(boolean condition) {
    if (!condition) {
      throw new AssumeException();
    }
  }
}
