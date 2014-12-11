package org.testanza;

import static org.testanza.Case.newCase;
import static org.testanza.Suite.newSuite;
import static org.testanza.Testers.asTester;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public abstract class SuiteTester<T> implements Tester<T> {
  private List<Test> tests;

  public Test test(final T item) {
    tests = new ArrayList<Test>();
    try {
      tests(item);
      return newSuite(name(item), tests);
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
    } finally {
      tests = null;
    }
  }

  protected abstract String name(T item);

  protected abstract void tests(T item) throws Throwable;

  protected <E> void testThat(E item, Tester<E> tester) {
    tests.add(tester.test(item));
  }

  protected <E> void testThatAll(List<? extends E> items, Tester<E> tester) {
    for (E item : items) {
      testThat(item, tester);
    }
  }

  protected <E> void testThatAll(E[] items, Tester<E> tester) {
    for (E item : items) {
      testThat(item, tester);
    }
  }

  protected <E> void testThat(E item, Matcher<E> matcher) {
    tests.add(asTester(matcher).test(item));
  }

  protected <E> void testThatAll(List<? extends E> items, Matcher<E> matcher) {
    testThatAll(items, asTester(matcher));
  }

  protected <E> void testThatAll(E[] items, Matcher<E> matcher) {
    testThatAll(items, asTester(matcher));
  }
}
