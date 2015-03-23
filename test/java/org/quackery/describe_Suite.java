package org.quackery;

import static java.util.Arrays.asList;
import static java.util.Objects.hash;
import static org.quackery.Suite.newSuite;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockObject;

public class describe_Suite {
  private String name = "name";
  private final Test //
      testA = new Test() {}, //
      testB = new Test() {}, //
      testC = new Test() {}, //
      testD = new Test() {};
  private final Object //
      itemA = mockObject("itemA"), //
      itemB = mockObject("itemB"), //
      itemC = mockObject("itemC"), //
      itemD = mockObject("itemD");
  private final Tester<Object> //
      testerA = new MockTester<Object>("testerA"), //
      testerB = new MockTester<Object>("testerB"), //
      testerC = new MockTester<Object>("testerC");
  private Suite suite;

  public void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Suite.class));
  }

  public void creates_empty_suite() {
    suite = newSuite(name);
    assertEquals(suite.tests, asList());
  }

  public void assigns_name() {
    suite = newSuite(name);
    assertEquals(name, suite.name);
  }

  public void tests_test() {
    suite = newSuite(name) //
        .test(testA) //
        .test(testB) //
        .test(testC);
    assertEquals(suite.tests, asList(testA, testB, testC));
  }

  public void tests_all_tests_in_iterable() {
    suite = newSuite(name) //
        .testAll(asList(testA, testB)) //
        .testAll(asList(testC, testD));
    assertEquals(suite.tests, asList(testA, testB, testC, testD));
  }

  public void tests_all_tests_in_array() {
    suite = newSuite(name) //
        .testAll(new Test[] { testA, testB }) //
        .testAll(new Test[] { testC, testD });
    assertEquals(suite.tests, asList(testA, testB, testC, testD));
  }

  public void tests_that_item() {
    suite = newSuite(name) //
        .testThat(itemA, testerA) //
        .testThat(itemB, testerB) //
        .testThat(itemC, testerC);
    assertEquals(suite.tests, asList( //
        new MockTest(itemA, testerA), //
        new MockTest(itemB, testerB), //
        new MockTest(itemC, testerC)));
  }

  public void tests_that_all_items_in_iterable() {
    suite = newSuite(name) //
        .testThatAll(asList(itemA, itemB), testerA) //
        .testThatAll(asList(itemC, itemD), testerB);
    assertEquals(suite.tests, asList( //
        new MockTest(itemA, testerA), //
        new MockTest(itemB, testerA), //
        new MockTest(itemC, testerB), //
        new MockTest(itemD, testerB)));
  }

  public void tests_that_all_items_in_array() {
    suite = newSuite(name) //
        .testThatAll(new Object[] { itemA, itemB }, testerA) //
        .testThatAll(new Object[] { itemC, itemD }, testerB);
    assertEquals(suite.tests, asList( //
        new MockTest(itemA, testerA), //
        new MockTest(itemB, testerA), //
        new MockTest(itemC, testerB), //
        new MockTest(itemD, testerB)));
  }

  public void lists_are_covariant() {
    class Foo {}
    class Bar extends Foo {}
    final Bar bar = null;
    final Tester<Foo> fooTester = null;

    // don't run, just compile
    new Runnable() {
      public void run() {
        newSuite(name) //
            .testAll(asList(new Case[0])) //
            .testThatAll(asList(bar), fooTester); //
      }
    };
  }

  public void to_string_returns_name() {
    suite = newSuite(name);
    assertEquals(name, suite.toString());
  }

  public void name_cannot_be_null() {
    name = null;
    try {
      newSuite(name);
      fail();
    } catch (QuackeryException e) {}
  }

  public void test_cannot_be_null() {
    suite = newSuite(name);
    try {
      suite.test(null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testAll((Iterable<Test>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testAll(asList(testA, null, testB));
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testAll((Test[]) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testAll(new Test[] { testA, null, testB });
      fail();
    } catch (QuackeryException e) {}
  }

  public void items_cannot_be_null() {
    suite = newSuite(name);
    try {
      suite.testThatAll((Iterable<Object>) null, testerA);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testThatAll((Object[]) null, testerA);
      fail();
    } catch (QuackeryException e) {}
  }

  public void tester_cannot_be_null() {
    suite = newSuite(name);
    try {
      suite.testThat(itemA, (Tester<Object>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testThatAll(asList(), (Tester<Object>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testThatAll(new Object[0], (Tester<Object>) null);
      fail();
    } catch (QuackeryException e) {}
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