package org.quackery;

import static java.util.Arrays.asList;
import static java.util.Objects.hash;
import static org.quackery.Suite.newSuite;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockObject;

public class test_Suite {
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
  private final Contract<Object> //
      contractA = new MockContract<Object>("contractA"), //
      contractB = new MockContract<Object>("contractB"), //
      contractC = new MockContract<Object>("contractC");
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
        .testThat(itemA, contractA) //
        .testThat(itemB, contractB) //
        .testThat(itemC, contractC);
    assertEquals(suite.tests, asList( //
        new MockTest(itemA, contractA), //
        new MockTest(itemB, contractB), //
        new MockTest(itemC, contractC)));
  }

  public void tests_that_all_items_in_iterable() {
    suite = newSuite(name) //
        .testThatAll(asList(itemA, itemB), contractA) //
        .testThatAll(asList(itemC, itemD), contractB);
    assertEquals(suite.tests, asList( //
        new MockTest(itemA, contractA), //
        new MockTest(itemB, contractA), //
        new MockTest(itemC, contractB), //
        new MockTest(itemD, contractB)));
  }

  public void tests_that_all_items_in_array() {
    suite = newSuite(name) //
        .testThatAll(new Object[] { itemA, itemB }, contractA) //
        .testThatAll(new Object[] { itemC, itemD }, contractB);
    assertEquals(suite.tests, asList( //
        new MockTest(itemA, contractA), //
        new MockTest(itemB, contractA), //
        new MockTest(itemC, contractB), //
        new MockTest(itemD, contractB)));
  }

  public void lists_are_covariant() {
    class Foo {}
    class Bar extends Foo {}
    final Bar bar = null;
    final Contract<Foo> fooContract = null;

    // don't run, just compile
    new Runnable() {
      public void run() {
        newSuite(name) //
            .testAll(asList(new Case[0])) //
            .testThatAll(asList(bar), fooContract); //
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
      suite.testThatAll((Iterable<Object>) null, contractA);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testThatAll((Object[]) null, contractA);
      fail();
    } catch (QuackeryException e) {}
  }

  public void contract_cannot_be_null() {
    suite = newSuite(name);
    try {
      suite.testThat(itemA, (Contract<Object>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testThatAll(asList(), (Contract<Object>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.testThatAll(new Object[0], (Contract<Object>) null);
      fail();
    } catch (QuackeryException e) {}
  }

  private static class MockContract<T> implements Contract<T> {
    private final String name;

    public MockContract(String name) {
      this.name = name;
    }

    public Test test(T item) {
      return new MockTest(item, this);
    }

    public String toString() {
      return "MockContract(" + name + ")";
    }
  }

  private static class MockTest implements Test {
    public final Object item;
    public final Contract<?> contract;

    public MockTest(Object item, Contract<?> contract) {
      this.item = item;
      this.contract = contract;
    }

    public boolean equals(Object object) {
      return object instanceof MockTest && equals((MockTest) object);
    }

    public boolean equals(MockTest that) {
      return item.equals(that.item) && contract.equals(that.contract);
    }

    public int hashCode() {
      return hash(item, contract);
    }

    public String toString() {
      return "MockTest(" + item + ", " + contract + ")";
    }
  }
}
