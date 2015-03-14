package org.quackery.testing;

import java.util.Objects;

public class Assertions {
  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }

  public static void assertEquals(Object actual, Object expected) {
    if (!Objects.deepEquals(actual, expected)) {
      throw new AssertionError("" //
          + "\n" //
          + "  expected that\n" //
          + "    " + actual + "\n" //
          + "  is equal to\n" //
          + "    " + expected + "\n" //
      );
    }
  }

  public static void assertNotEquals(Object actual, Object expected) {
    if (Objects.deepEquals(actual, expected)) {
      throw new AssertionError("" //
          + "\n" //
          + "  expected that\n" //
          + "    " + actual + "\n" //
          + "  is not equal to\n" //
          + "    " + expected + "\n" //
      );
    }
  }

  public static void fail() {
    throw new AssertionError();
  }
}
