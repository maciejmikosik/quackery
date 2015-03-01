package org.testanza;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Testilities {
  public static Object newObject(final String name) {
    return new Object() {
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
}
