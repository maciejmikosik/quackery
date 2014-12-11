package org.testanza;

public class TestanzaAssertionError extends AssertionError {
  public TestanzaAssertionError() {}

  public TestanzaAssertionError(String message) {
    super(message);
  }

  public TestanzaAssertionError(String message, Throwable cause) {
    super(message, cause);
  }
}
