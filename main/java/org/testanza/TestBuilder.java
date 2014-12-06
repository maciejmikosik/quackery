package org.testanza;

import static org.testanza.Testers.asTester;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.hamcrest.Matcher;

// TODO untested
public class TestBuilder {
  private final TestSuite suite;

  public TestBuilder(String name) {
    this.suite = new TestSuite(name);
  }

  public <T> void testThat(T item, Tester<T> tester) {
    suite.addTest(tester.test(item));
  }

  public <T> void testThatAll(Iterable<? extends T> items, Tester<T> tester) {
    for (T item : items) {
      suite.addTest(tester.test(item));
    }
  }

  public <T> void testThatAll(T[] items, Tester<T> tester) {
    for (T item : items) {
      suite.addTest(tester.test(item));
    }
  }

  public <T> void testThat(T item, Matcher<T> matcher) {
    suite.addTest(asTester(matcher).test(item));
  }

  public <T> void testThatAll(Iterable<? extends T> items, Matcher<T> matcher) {
    for (T item : items) {
      suite.addTest(asTester(matcher).test(item));
    }
  }

  public <T> void testThatAll(T[] items, Matcher<T> matcher) {
    for (T item : items) {
      suite.addTest(asTester(matcher).test(item));
    }
  }

  public Test build() {
    return suite;
  }
}
