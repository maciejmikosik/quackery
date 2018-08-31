package org.quackery.report;

import static org.quackery.report.AssumeException.assume;

import org.quackery.testing.Testing;

public class TestAssumeException {
  public static void test_assume_exception() {
    assume(true);

    try {
      assume(false);
      Testing.fail();
    } catch (AssumeException e) {}
  }
}
