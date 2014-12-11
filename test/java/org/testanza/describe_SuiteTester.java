package org.testanza;

import static java.util.Arrays.asList;
import static org.testanza.Testilities.newMatcher;
import static org.testanza.Testilities.newObject;
import static org.testanza.Testilities.newTester;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;

import org.hamcrest.Matcher;

public class describe_SuiteTester {
  private final Test testA = new Test() {};
  private Tester<Object> testerA = newTester(testA);
  private final String string = "string", name = "name";
  private final Object a = newObject("a"), b = newObject("b"), c = newObject("c");
  private final Object object = newObject("item");
  private Matcher<Object> matcher;
  private final RuntimeException exception = new RuntimeException();

  private Tester<Object> tester;
  private Suite suite;
  private Case cas;

  public void uses_given_name() {
    tester = new SuiteTester<Object>() {
      protected String name(Object item) {
        return string + item;
      }

      protected void tests(Object item) throws Throwable {}
    };
    suite = (Suite) tester.test(object);
    verifyEquals(suite.name, string + object);
  }

  public void tests_single_item() {
    testerA = newTester(testA);
    tester = new SuiteTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void tests(Object item) {
        testThat(item, testerA);
      }
    };
    suite = (Suite) tester.test(object);
    verifyEquals(suite.tests.size(), 1);
    verifyEquals(suite.tests.get(0), testA);
  }

  public void tests_iterable_of_items() {
    testerA = newTester(testA);
    tester = new SuiteTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void tests(Object item) {
        testThatAll(asList(a, b, c), testerA);
      }
    };
    suite = (Suite) tester.test(object);
    verifyEquals(suite.tests.size(), 3);
    verifyEquals(suite.tests.get(0), testA);
    verifyEquals(suite.tests.get(1), testA);
    verifyEquals(suite.tests.get(2), testA);
  }

  public void tests_array_of_items() {
    testerA = newTester(testA);
    tester = new SuiteTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void tests(Object item) {
        testThatAll(new Object[] { a, b, c }, testerA);
      }
    };
    suite = (Suite) tester.test(object);
    verifyEquals(suite.tests.size(), 3);
    verifyEquals(suite.tests.get(0), testA);
    verifyEquals(suite.tests.get(1), testA);
    verifyEquals(suite.tests.get(2), testA);
  }

  public void converts_matcher_to_tester() throws Throwable {
    matcher = newMatcher(object);
    tester = new SuiteTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void tests(Object item) {
        testThat(object, matcher);
      }
    };
    suite = (Suite) tester.test(object);
    verifyEquals(suite.tests.size(), 1);
  }

  public void tester_instance_is_reusable() {
    tester = new SuiteTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void tests(Object item) throws Throwable {
        testThat(object, testerA);
        testThat(object, testerA);
      }
    };
    suite = (Suite) tester.test(object);
    verifyEquals(suite.tests.size(), 2);
    suite = (Suite) tester.test(object);
    verifyEquals(suite.tests.size(), 2);
  }

  public void lists_and_arrays_are_covariant() {
    class Foo {}
    class Bar extends Foo {}
    final Foo foo = null;
    final Bar bar = null;
    final Tester<Foo> fooTester = null;
    final Matcher<Foo> fooMatcher = null;

    new SuiteTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void tests(Object item) throws Throwable {
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

  public void exception_during_building_is_converted_to_failing_case() throws Throwable {
    tester = new SuiteTester<Object>() {
      protected String name(Object item) {
        return name;
      }

      protected void tests(Object item) throws Throwable {
        throw exception.fillInStackTrace();
      }
    };

    cas = (Case) tester.test(object);

    verifyEquals(cas.name, name);
    try {
      cas.body.invoke();
      verifyFail();
    } catch (TestanzaAssertionError e) {
      verify(e.getMessage().contains("" //
          + "\n" //
          + "  building suite\n" //
          + "    " + name + "\n" //
          + "  failed\n" //
      ));
      verifyEquals(e.getCause(), exception);
    }
  }
}
