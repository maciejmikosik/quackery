package org.testanza;

public class TestanzaAssertionException extends RuntimeException {
  public TestanzaAssertionException() {}

  public TestanzaAssertionException(String message) {
    super(message);
  }

  public TestanzaAssertionException(String message, Throwable cause) {
    super(message, cause);
  }
}
