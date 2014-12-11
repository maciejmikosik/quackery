package org.testanza;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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

  public static <T> Tester<T> newTester(final Test test) {
    return new Tester<T>() {
      public Test test(T item) {
        return test;
      }
    };
  }

  public static <T> Matcher<T> newMatcher(final Object object) {
    return new TypeSafeMatcher<T>() {
      public void describeTo(Description description) {}

      protected boolean matchesSafely(T item) {
        return object == item;
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
