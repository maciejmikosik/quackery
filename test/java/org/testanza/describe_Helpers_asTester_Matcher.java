package org.testanza;

import static org.testanza.Helpers.asTester;
import static org.testanza.testing.Assertions.assertEquals;
import static org.testanza.testing.Assertions.fail;
import static org.testanza.testing.Mocks.mockObject;
import static org.testanza.testing.Tests.name;
import static org.testanza.testing.Tests.run;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class describe_Helpers_asTester_Matcher {
  private Object item;
  private Matcher<Object> matcher;
  private Tester<Object> tester;
  private Test test;

  public void succeeds_if_matcher_matches() throws Throwable {
    item = mockObject("item");
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

    run(test);
  }

  public void fails_if_matcher_not_matches() throws Throwable {
    item = mockObject("item");
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

    try {
      run(test);
      fail();
    } catch (TestanzaAssertionError e) {}
  }

  public void failure_prints_message() throws Throwable {
    item = mockObject("item");
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

    try {
      run(test);
      fail();
    } catch (TestanzaAssertionError e) {
      assertEquals(e.getMessage(), "" //
          + "\n" //
          + "  expected that\n" //
          + "    " + item + "\n" //
          + "  matches\n" //
          + "    " + matcher + "\n" //
          + "  but\n" //
          + "    mismatch for " + item + "\n");
    }
  }

  public void test_name_contains_matcher_and_item() {
    item = mockObject("item");
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
    assertEquals(name(test), item + " is " + matcher);
  }

  public void matcher_cannot_be_null() {
    try {
      asTester(null);
      fail();
    } catch (TestanzaException e) {}
  }
}
