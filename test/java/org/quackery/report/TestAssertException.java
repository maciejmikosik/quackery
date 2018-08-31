package org.quackery.report;

import static java.lang.String.format;
import static org.quackery.report.AssertException.assertEquals;
import static org.quackery.report.AssertException.assertTrue;
import static org.quackery.report.AssertException.fail;
import static org.quackery.testing.Testing.mockObject;

import java.util.Arrays;

import org.quackery.testing.Testing;

public class TestAssertException {
  public static void test_assert_exception() {
    fail_fails();
    assert_true_handles_booleans();
    assert_equals_handles_objects();
    assert_equals_handles_deep_arrays();
    assert_equals_handles_null();
  }

  private static void fail_fails() {
    try {
      fail();
      Testing.fail();
    } catch (AssertException e) {}
  }

  private static void assert_true_handles_booleans() {
    assertTrue(true);

    try {
      assertTrue(false);
      Testing.fail();
    } catch (AssertException e) {}
  }

  private static void assert_equals_handles_objects() {
    Object expected = mockObject("expected");
    Object equal = mockObject("expected");
    Object notEqual = mockObject("notEqual");

    assertEquals(expected, expected);
    assertEquals(equal, expected);

    try {
      assertEquals(notEqual, expected);
      Testing.fail();
    } catch (AssertException e) {
      Testing.assertEquals(
          e.getMessage(),
          format("\n"
              + "  expected equal to\n"
              + "    %s\n"
              + "  but was\n"
              + "    %s\n",
              expected,
              notEqual));
    }
  }

  private static void assert_equals_handles_deep_arrays() {
    String[][] expected = new String[][] { { "expected" } };
    String[][] equal = new String[][] { { "expected" } };
    String[][] notEqual = new String[][] { { "notEqual" } };

    assertEquals(expected, expected);
    assertEquals(equal, expected);

    try {
      assertEquals(notEqual, expected);
      Testing.fail();
    } catch (AssertException e) {
      Testing.assertEquals(
          e.getMessage(),
          format("\n"
              + "  expected equal to\n"
              + "    %s\n"
              + "  but was\n"
              + "    %s\n",
              Arrays.deepToString(expected),
              Arrays.deepToString(notEqual)));
    }
  }

  private static void assert_equals_handles_null() {
    assertEquals(null, null);

    try {
      assertEquals(null, "string");
      Testing.fail();
    } catch (AssertException e) {
      Testing.assertEquals(e.getMessage(), "\n"
          + "  expected equal to\n"
          + "    string\n"
          + "  but was\n"
          + "    null\n");
    }

    try {
      assertEquals("string", null);
      Testing.fail();
    } catch (AssertException e) {
      Testing.assertEquals(e.getMessage(), "\n"
          + "  expected equal to\n"
          + "    null\n"
          + "  but was\n"
          + "    string\n");
    }
  }
}
