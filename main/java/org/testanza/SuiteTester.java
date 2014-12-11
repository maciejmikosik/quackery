package org.testanza;

import static org.testanza.Suite.newSuite;
import static org.testanza.Testers.asTester;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public abstract class SuiteTester<T> implements Tester<T> {
  private final List<Test> tests = new ArrayList<Test>();

  public Test test(T item) {
    try {
      tests(item);
    } catch (Throwable e) {
      throw new TestanzaException("failed to created suite", e);
    }
    return newSuite("", tests);
  }

  protected abstract void tests(T item) throws Throwable;

  protected void testThat(T item, Tester<T> tester) {
    tests.add(tester.test(item));
  }

  protected void testThatAll(List<? extends T> items, Tester<T> tester) {
    for (T item : items) {
      testThat(item, tester);
    }
  }

  protected void testThatAll(T[] items, Tester<T> tester) {
    for (T item : items) {
      testThat(item, tester);
    }
  }

  protected void testThat(T item, Matcher<T> matcher) {
    tests.add(asTester(matcher).test(item));
  }

  protected void testThatAll(List<? extends T> items, Matcher<T> matcher) {
    testThatAll(items, asTester(matcher));
  }

  protected void testThatAll(T[] items, Matcher<T> matcher) {
    testThatAll(items, asTester(matcher));
  }
}
