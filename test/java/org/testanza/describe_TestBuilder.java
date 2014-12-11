package org.testanza;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class describe_TestBuilder {
  private TestBuilder builder;

  public void accepts_covariant_list() {
    List<String> items = Arrays.asList();
    Tester<Object> tester = new Tester<Object>() {
      public Test test(Object item) {
        return new Test() {};
      }
    };
    Matcher<Object> matcher = new TypeSafeMatcher<Object>() {
      public void describeTo(Description description) {}

      protected boolean matchesSafely(Object item) {
        return false;
      }
    };
    builder = new TestBuilder("");
    // verify that compiles
    builder.testThatAll(items, tester);
    builder.testThatAll(items, matcher);
  }
}
