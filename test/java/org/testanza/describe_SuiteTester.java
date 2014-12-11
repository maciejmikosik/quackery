package org.testanza;

import static java.util.Arrays.asList;
import static org.testanza.Testilities.newMatcher;
import static org.testanza.Testilities.newObject;
import static org.testanza.Testilities.newTester;
import static org.testanza.Testilities.verifyEquals;

import org.hamcrest.Matcher;

public class describe_SuiteTester {
  private final Test testA = new Test() {};
  private Tester<Object> testerA = newTester(testA);
  private final Object a = newObject("a"), b = newObject("b"), c = newObject("c");
  private final Object item = newObject("item");
  private Matcher<Object> matcher;

  private Tester<Object> tester;
  private Suite suite;

  public void tests_single_item() {
    testerA = newTester(testA);
    tester = new SuiteTester<Object>() {
      protected void tests(Object i) {
        testThat(i, testerA);
      }
    };
    suite = (Suite) tester.test(item);
    verifyEquals(suite.tests.size(), 1);
    verifyEquals(suite.tests.get(0), testA);
  }

  public void tests_iterable_of_items() {
    testerA = newTester(testA);
    tester = new SuiteTester<Object>() {
      protected void tests(Object i) {
        testThatAll(asList(a, b, c), testerA);
      }
    };
    suite = (Suite) tester.test(item);
    verifyEquals(suite.tests.size(), 3);
    verifyEquals(suite.tests.get(0), testA);
    verifyEquals(suite.tests.get(1), testA);
    verifyEquals(suite.tests.get(2), testA);
  }

  public void tests_array_of_items() {
    testerA = newTester(testA);
    tester = new SuiteTester<Object>() {
      protected void tests(Object i) {
        testThatAll(new Object[] { a, b, c }, testerA);
      }
    };
    suite = (Suite) tester.test(item);
    verifyEquals(suite.tests.size(), 3);
    verifyEquals(suite.tests.get(0), testA);
    verifyEquals(suite.tests.get(1), testA);
    verifyEquals(suite.tests.get(2), testA);
  }

  public void converts_matcher_to_tester() throws Throwable {
    matcher = newMatcher(item);
    tester = new SuiteTester<Object>() {
      protected void tests(Object i) {
        testThat(item, matcher);
      }
    };
    suite = (Suite) tester.test(item);
    verifyEquals(suite.tests.size(), 1);
  }

  public void lists_and_arrays_are_covariant() {
    class Foo {}
    class Bar extends Foo {}
    final Foo foo = null;
    final Bar bar = null;
    final Tester<Foo> fooTester = null;
    final Matcher<Foo> fooMatcher = null;

    new SuiteTester<Foo>() {
      protected void tests(Foo i) throws Throwable {
        testThat(foo, fooTester);
        testThat(bar, fooTester);
        testThatAll(asList(foo), fooTester);
        testThatAll(asList(bar), fooTester);
        testThatAll(new Foo[0], fooTester);
        testThatAll(new Bar[0], fooTester);
        testThat(foo, fooMatcher);
        testThat(bar, fooMatcher);
        testThatAll(asList(foo), fooMatcher);
        testThatAll(asList(bar), fooMatcher);
        testThatAll(new Foo[0], fooMatcher);
        testThatAll(new Bar[0], fooMatcher);
      }
    };
  }
}
