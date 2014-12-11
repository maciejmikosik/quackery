package org.testanza;

import static org.testanza.Case.newCase;
import static org.testanza.Suite.newSuite;
import static org.testanza.Testers.asTester;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public abstract class SuiteTester<T> implements Tester<T> {
  private final List<Test> tests = new ArrayList<Test>();

  public Test test(final T item) {
    try {
      tests(item);
    } catch (final Throwable e) {
      return newCase(name(item), new Closure() {
        public void invoke() throws Throwable {
          throw new TestanzaAssertionError("" //
              + "\n" //
              + "  building suite\n" //
              + "    " + name(item) + "\n" //
              + "  failed\n" //
          , e);
        }
      });
    }
    return newSuite(name(item), tests);
  }

  protected abstract String name(T item);

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
