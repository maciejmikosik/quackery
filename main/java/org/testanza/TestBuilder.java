package org.testanza;

import static org.testanza.Suite.newSuite;
import static org.testanza.Testers.asTester;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

// TODO untested
public class TestBuilder {
  private final String name;
  private final List<Test> tests = new ArrayList<Test>();

  public TestBuilder(String name) {
    this.name = name;
  }

  public <T> void testThat(T item, Tester<T> tester) {
    tests.add(tester.test(item));
  }

  public <T> void testThatAll(Iterable<? extends T> items, Tester<T> tester) {
    for (T item : items) {
      tests.add(tester.test(item));
    }
  }

  public <T> void testThatAll(T[] items, Tester<T> tester) {
    for (T item : items) {
      tests.add(tester.test(item));
    }
  }

  public <T> void testThat(T item, Matcher<T> matcher) {
    tests.add(asTester(matcher).test(item));
  }

  public <T> void testThatAll(Iterable<? extends T> items, Matcher<T> matcher) {
    for (T item : items) {
      tests.add(asTester(matcher).test(item));
    }
  }

  public <T> void testThatAll(T[] items, Matcher<T> matcher) {
    for (T item : items) {
      tests.add(asTester(matcher).test(item));
    }
  }

  public Test build() {
    return newSuite(name, tests);
  }
}
