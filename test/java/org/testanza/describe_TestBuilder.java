package org.testanza;

import static java.util.Arrays.asList;
import static java.util.Objects.hash;
import static org.testanza.Testilities.name;
import static org.testanza.Testilities.newMatcher;
import static org.testanza.Testilities.newObject;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;

import java.util.ArrayList;

import org.hamcrest.Matcher;

public class describe_TestBuilder {
  private TestBuilder builder = new TestBuilder("");
  private Test test;
  private final String name = "name";
  private final Tester<Object> tester = new MockTester<Object>("tester");
  private final Object object = newObject("object");
  private final Object a = newObject("a"), b = newObject("b"), c = newObject("c");
  private final Matcher<Object> matcher = newMatcher(object);

  public void uses_given_name() {
    builder = new TestBuilder(name);
    test = builder.build();
    verifyEquals(name(test), name);
  }

  public void creates_empty_suite() {
    builder = new TestBuilder("");
    test = builder.build();
    verify(test instanceof Suite);
    verifyEquals(((Suite) test).tests, new ArrayList<>());
  }

  public void tests_single_item() {
    builder.testThat(object, tester);
    test = builder.build();
    verifyEquals(((Suite) test).tests, asList(new MockTest(object, tester)));
  }

  public void tests_iterable_of_items() {
    builder.testThatAll(asList(a, b, c), tester);
    test = builder.build();
    verifyEquals(((Suite) test).tests, //
        asList( //
            new MockTest(a, tester), //
            new MockTest(b, tester), //
            new MockTest(c, tester) //
        ) //
    );
  }

  public void tests_array_of_items() {
    builder.testThatAll(new Object[] { a, b, c }, tester);
    test = builder.build();
    verifyEquals(((Suite) test).tests, //
        asList( //
            new MockTest(a, tester), //
            new MockTest(b, tester), //
            new MockTest(c, tester) //
        ) //
    );
  }

  public void tests_against_matcher() {
    builder.testThat(object, matcher);
    builder.testThatAll(new Object[] { a, b }, matcher);
    builder.testThatAll(asList(a, b, c), matcher);
    test = builder.build();
    verifyEquals(((Suite) test).tests.size(), 6);
  }

  public void lists_and_arrays_are_covariant() {
    class Foo {}
    class Bar extends Foo {}
    final Foo foo = null;
    final Bar bar = null;
    final Tester<Foo> fooTester = null;
    final Matcher<Foo> fooMatcher = null;

    // don't run, just compile
    new Runnable() {
      public void run() {
        builder.testThat(foo, fooTester);
        builder.testThat(bar, fooTester);
        builder.testThatAll(asList(foo), fooTester);
        builder.testThatAll(asList(bar), fooTester);
        builder.testThatAll(new Foo[0], fooTester);
        builder.testThatAll(new Bar[0], fooTester);
        builder.testThat(foo, fooMatcher);
        builder.testThat(bar, fooMatcher);
        builder.testThatAll(asList(foo), fooMatcher);
        builder.testThatAll(asList(bar), fooMatcher);
        builder.testThatAll(new Foo[0], fooMatcher);
        builder.testThatAll(new Bar[0], fooMatcher);
      }
    };
  }

  public void name_cannot_be_null() {
    try {
      builder = new TestBuilder(null);
      verifyFail();
    } catch (TestanzaException e) {}
  }

  public void tester_cannot_be_null() {
    try {
      builder.testThat(object, (Tester<Object>) null);
      verifyFail();
    } catch (TestanzaException e) {}
    try {
      builder.testThatAll(asList(object), (Tester<Object>) null);
      verifyFail();
    } catch (TestanzaException e) {}
    try {
      builder.testThat(asList(object), (Tester<Object>) null);
      verifyFail();
    } catch (TestanzaException e) {}
  }

  public void matcher_cannot_be_null() {
    try {
      builder.testThat(object, (Matcher<Object>) null);
      verifyFail();
    } catch (TestanzaException e) {}
    try {
      builder.testThatAll(asList(object), (Matcher<Object>) null);
      verifyFail();
    } catch (TestanzaException e) {}
    try {
      builder.testThat(asList(object), (Matcher<Object>) null);
      verifyFail();
    } catch (TestanzaException e) {}
  }

  private static class MockTester<T> implements Tester<T> {
    private final String name;

    public MockTester(String name) {
      this.name = name;
    }

    public Test test(T item) {
      return new MockTest(item, this);
    }

    public String toString() {
      return "MockTester(" + name + ")";
    }
  }

  private static class MockTest implements Test {
    public final Object item;
    public final Tester<?> tester;

    public MockTest(Object item, Tester<?> tester) {
      this.item = item;
      this.tester = tester;
    }

    public boolean equals(Object object) {
      return object instanceof MockTest && equals((MockTest) object);
    }

    public boolean equals(MockTest that) {
      return item.equals(that.item) && tester.equals(that.tester);
    }

    public int hashCode() {
      return hash(item, tester);
    }

    public String toString() {
      return "MockTest(" + item + ", " + tester + ")";
    }
  }
}
