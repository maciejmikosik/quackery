package org.testanza;

public class TestanzaAssertionError extends AssertionError {
  public TestanzaAssertionError() {}

  public TestanzaAssertionError(String message) {
    super(message);
  }
}
