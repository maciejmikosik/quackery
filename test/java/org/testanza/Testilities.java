package org.testanza;

import java.util.Objects;

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

  public static Closure newClosure(final String name) {
    return new Closure() {
      public void invoke() {}

      public String toString() {
        return name;
      }
    };
  }

  public static String name(Test test) {
    return test instanceof Case
        ? ((Case) test).name
        : test instanceof Suite
            ? ((Suite) test).name
            : unknown(String.class);
  }

  private static <T> T unknown(Class<T> type) {
    throw new RuntimeException("" + type);
  }
}
