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

  public static String name(Test test) {
    return test instanceof Case
        ? ((Case) test).name
        : test instanceof Suite
            ? ((Suite) test).name
            : unknown(String.class);
  }

  public static void run(Test test) throws Throwable {
    if (test instanceof Case) {
      ((Case) test).run();
    } else if (test instanceof Suite) {
      for (Test subtest : ((Suite) test).tests) {
        run(subtest);
      }
    } else {
      unknown(test.getClass());
    }
  }

  private static <T> T unknown(Class<T> type) {
    throw new RuntimeException("" + type);
  }
}
