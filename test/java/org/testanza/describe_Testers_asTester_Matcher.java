package org.testanza;

import static org.testanza.Testers.asTester;
import static org.testanza.Testilities.name;
import static org.testanza.Testilities.newObject;
import static org.testanza.Testilities.verifyEquals;
import junit.framework.Test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class describe_Testers_asTester_Matcher {
  private Object item;
  private Matcher<Object> matcher;
  private Tester<Object> tester;
  private Test test;
  private Result result;

  public void succeeds_if_matcher_matches() {
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
    verifyEquals(result.getFailureCount(), 0);
  }

  public void fails_if_matcher_not_matches() {
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
    verifyEquals(result.getFailureCount(), 1);
  }

  public void failure_prints_message() {
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
    verifyEquals(result.getFailures().get(0).getMessage(), "" //
        + "  expected that\n" //
        + "    " + item + "\n" //
        + "  matches\n" //
        + "    " + matcher + "\n" //
        + "  but\n" //
        + "    mismatch for " + item + "\n" //
    );
  }

  public void test_name_contains_matcher_and_item() {
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
    verifyEquals(name(test), item + " is " + matcher);
  }
}
