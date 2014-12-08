package org.testanza;

import java.util.Objects;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Testilities {
  public static void verify(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }

  public static void verifyFail() {
    throw new AssertionError();
  }

  public static void verifyEquals(Object actual, Object expected) {
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

  public static void verifyNotEquals(Object actual, Object expected) {
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

  public static Object newObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }

  public static String name(Test test) {
    return test instanceof TestCase
        ? ((TestCase) test).getName()
        : test instanceof TestSuite
            ? ((TestSuite) test).getName()
            : unknown(String.class);
  }

  private static <T> T unknown(Class<T> type) {
    throw new RuntimeException("" + type);
  }
}
