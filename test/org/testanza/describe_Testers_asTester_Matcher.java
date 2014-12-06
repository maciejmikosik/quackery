package org.testanza;

import static org.testanza.Testers.asTester;
import static org.testanza.describe_testanza.verify;
import junit.framework.Test;
import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class describe_Testers_asTester_Matcher {
  private static Object item;
  private static Matcher<Object> matcher;
  private static Tester<Object> tester;
  private static Test test;
  private static Result result;

  public static void succeeds_if_matcher_matches() {
    item = newObject("item");
    matcher = new TypeSafeMatcher<Object>() {
      public void describeTo(Description description) {}

      protected boolean matchesSafely(Object i) {
        if (i == item) {
          return true;
        }
        throw new RuntimeException();
      }
    };
    tester = asTester(matcher);
    test = tester.test(item);
    result = new JUnitCore().run(test);
    verify(result.getFailureCount() == 0);
  }

  public static void fails_if_matcher_not_matches() {
    item = newObject("item");
    matcher = new TypeSafeMatcher<Object>() {
      public void describeTo(Description description) {}

      protected boolean matchesSafely(Object i) {
        if (i == item) {
          return false;
        }
        throw new RuntimeException();
      }
    };
    tester = asTester(matcher);
    test = tester.test(item);
    result = new JUnitCore().run(test);
    verify(result.getFailureCount() == 1);
  }

  public static void failure_prints_message() {
    item = newObject("item");
    matcher = new TypeSafeMatcher<Object>() {
      public void describeTo(Description description) {
        description.appendText("failure_prints_message");
      }

      protected boolean matchesSafely(Object i) {
        if (i == item) {
          return false;
        }
        throw new RuntimeException();
      }

      protected void describeMismatchSafely(Object i, Description description) {
        description.appendText("mismatch for " + i);
      }
    };
    tester = asTester(matcher);
    test = tester.test(item);
    result = new JUnitCore().run(test);
    verify(result.getFailureCount() == 1);
    verify(result.getFailures().get(0).getMessage().equals("" //
        + "  expected that\n" //
        + "    " + item + "\n" //
        + "  matches\n" //
        + "    " + matcher + "\n" //
        + "  but\n" //
        + "    mismatch for " + item + "\n" //
    ));
  }

  public static void test_name_contains_matcher_and_item() {
    item = newObject("item");
    matcher = new TypeSafeMatcher<Object>() {
      public void describeTo(Description description) {
        description.appendText("test_name_contains_matcher_and_item");
      }

      protected boolean matchesSafely(Object i) {
        throw new RuntimeException();
      }
    };
    tester = asTester(matcher);
    test = tester.test(item);
    verify((item + " is " + matcher).equals(nameOf(test)));
  }

  private static Object newObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }

  private static String nameOf(Test testCase) {
    return ((TestCase) testCase).getName();
  }
}
