package org.quackery;

import static java.util.Arrays.asList;
import static org.quackery.Suite.suite;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockCase;
import static org.quackery.testing.Mocks.mockContract;
import static org.quackery.testing.Mocks.mockObject;
import static org.quackery.testing.Mocks.mockTest;

public class test_Suite {
  private String name = "name";
  private final Test
      testA = mockCase("testA"),
      testB = mockCase("testB"),
      testC = mockCase("testC"),
      testD = mockCase("testD");
  private final Object
      itemA = mockObject("itemA"),
      itemB = mockObject("itemB"),
      itemC = mockObject("itemC"),
      itemD = mockObject("itemD");
  private final Contract<Object>
      contractA = mockContract("contractA"),
      contractB = mockContract("contractB"),
      contractC = mockContract("contractC");
  private Suite suite;

  public void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Suite.class));
  }

  public void creates_empty_suite() {
    suite = suite(name);
    assertEquals(suite.tests, asList());
  }

  public void assigns_name() {
    suite = suite(name);
    assertEquals(name, suite.name);
  }

  public void tests_test() {
    suite = suite(name)
        .test(testA)
        .test(testB)
        .test(testC);
    assertEquals(suite.tests, asList(testA, testB, testC));
  }

  public void tests_all_tests_in_iterable() {
    suite = suite(name)
        .testAll(asList(testA, testB))
        .testAll(asList(testC, testD));
    assertEquals(suite.tests, asList(testA, testB, testC, testD));
  }

  public void tests_all_tests_in_array() {
    suite = suite(name)
        .testAll(new Test[] { testA, testB })
        .testAll(new Test[] { testC, testD });
    assertEquals(suite.tests, asList(testA, testB, testC, testD));
  }

  public void tests_that_item() {
    suite = suite(name)
        .testThat(itemA, contractA)
        .testThat(itemB, contractB)
        .testThat(itemC, contractC);
    assertEquals(suite.tests, asList(
        mockTest(itemA, contractA),
        mockTest(itemB, contractB),
        mockTest(itemC, contractC)));
  }

  public void tests_that_all_items_in_iterable() {
    suite = suite(name)
        .testThatAll(asList(itemA, itemB), contractA)
        .testThatAll(asList(itemC, itemD), contractB);
    assertEquals(suite.tests, asList(
        mockTest(itemA, contractA),
        mockTest(itemB, contractA),
        mockTest(itemC, contractB),
        mockTest(itemD, contractB)));
  }

  public void tests_that_all_items_in_array() {
    suite = suite(name)
        .testThatAll(new Object[] { itemA, itemB }, contractA)
        .testThatAll(new Object[] { itemC, itemD }, contractB);
    assertEquals(suite.tests, asList(
        mockTest(itemA, contractA),
        mockTest(itemB, contractA),
        mockTest(itemC, contractB),
        mockTest(itemD, contractB)));
  }

  public void lists_are_covariant() {
    class Foo {}
    class Bar extends Foo {}
    final Bar bar = null;
    final Contract<Foo> fooContract = null;

    // don't run, just compile
    new Runnable() {
      public void run() {
        suite(name)
            .testAll(asList(new Case[0]))
            .testThatAll(asList(bar), fooContract);
      }
    };
  }

  public void to_string_returns_name() {
    suite = suite(name);
    assertEquals(name, suite.toString());
  }

  public void name_cannot_be_null() {
    name = null;
    try {
      suite(name);
      fail();
    } catch (QuackeryException e) {}
  }

  public void test_cannot_be_null() {
    suite = suite(name);
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
    suite = suite(name);
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
    suite = suite(name);
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
}
