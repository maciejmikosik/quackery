package org.testanza;

import static org.testanza.Suite.newSuite;
import static org.testanza.TestanzaException.check;
import static org.testanza.Testers.asTester;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public class TestBuilder {
  private final String name;
  private final List<Test> tests = new ArrayList<>();

  public TestBuilder(String name) {
    check(name != null);
    this.name = name;
  }

  public Test build() {
    return newSuite(name, tests);
  }

  public <T> void testThat(T item, Tester<T> tester) {
    check(tester != null);
    tests.add(tester.test(item));
  }

  public <T> void testThatAll(Iterable<? extends T> items, Tester<T> tester) {
    check(tester != null);
    for (T item : items) {
      testThat(item, tester);
    }
  }

  public <T> void testThatAll(T[] items, Tester<T> tester) {
    check(tester != null);
    for (T item : items) {
      testThat(item, tester);
    }
  }

  public <T> void testThat(T item, Matcher<T> matcher) {
    check(matcher != null);
    testThat(item, asTester(matcher));
  }

  public <T> void testThatAll(Iterable<? extends T> items, Matcher<T> matcher) {
    check(matcher != null);
    testThatAll(items, asTester(matcher));
  }

  public <T> void testThatAll(T[] items, Matcher<T> matcher) {
    check(matcher != null);
    testThatAll(items, asTester(matcher));
  }
}
