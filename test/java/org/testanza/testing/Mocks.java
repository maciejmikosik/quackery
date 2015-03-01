package org.testanza.testing;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Mocks {
  public static Object mockObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }

  public static <T> Matcher<T> mockMatcher(final Object object) {
    return new TypeSafeMatcher<T>() {
      public void describeTo(Description description) {}

      protected boolean matchesSafely(T item) {
        return object == item;
      }
    };
  }
}
