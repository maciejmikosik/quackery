package org.quackery;

import static java.util.Arrays.asList;
import static org.quackery.Suite.suite;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;
import static org.quackery.testing.Testing.mockContract;
import static org.quackery.testing.Testing.mockObject;

public class TestSuite {
  private String name = "name";
  private final Test testA = mockCase("testA"),
      testB = mockCase("testB"),
      testC = mockCase("testC"),
      testD = mockCase("testD");
  private final Object itemA = mockObject("itemA"),
      itemB = mockObject("itemB"),
      itemC = mockObject("itemC"),
      itemD = mockObject("itemD");
  private final Contract<Object> contractA = mockContract("contractA"),
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

  public void has_name() {
    suite = suite(name);
    assertEquals(suite.name, name);
  }

  public void adds_test() {
    suite = suite(name)
        .add(testA)
        .add(testB)
        .add(testC);
    assertEquals(suite.tests, asList(testA, testB, testC));
  }

  public void adds_tests_from_iterable() {
    suite = suite(name)
        .addAll(asList(testA, testB))
        .addAll(asList(testC, testD));
    assertEquals(suite.tests, asList(testA, testB, testC, testD));
  }

  public void adds_tests_from_array() {
    suite = suite(name)
        .addAll(new Test[] { testA, testB })
        .addAll(new Test[] { testC, testD });
    assertEquals(suite.tests, asList(testA, testB, testC, testD));
  }

  public void adds_test_produced_by_contract_and_item() {
    suite = suite(name)
        .add(itemA, contractA)
        .add(itemB, contractB)
        .add(itemC, contractC);
    assertEquals(suite.tests, asList(
        contractA.test(itemA),
        contractB.test(itemB),
        contractC.test(itemC)));
  }

  public void adds_tests_produced_by_contract_and_items_from_iterable() {
    suite = suite(name)
        .addAll(asList(itemA, itemB), contractA)
        .addAll(asList(itemC, itemD), contractB);
    assertEquals(suite.tests, asList(
        contractA.test(itemA),
        contractA.test(itemB),
        contractB.test(itemC),
        contractB.test(itemD)));
  }

  public void adds_tests_produced_by_contract_and_items_from_array() {
    suite = suite(name)
        .addAll(new Object[] { itemA, itemB }, contractA)
        .addAll(new Object[] { itemC, itemD }, contractB);
    assertEquals(suite.tests, asList(
        contractA.test(itemA),
        contractA.test(itemB),
        contractB.test(itemC),
        contractB.test(itemD)));
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
            .addAll(asList(new Case[0]))
            .addAll(asList(bar), fooContract);
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
      suite.add(null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll((Iterable<Test>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll(asList(testA, null, testB));
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll((Test[]) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll(new Test[] { testA, null, testB });
      fail();
    } catch (QuackeryException e) {}
  }

  public void items_cannot_be_null() {
    suite = suite(name);
    try {
      suite.addAll((Iterable<Object>) null, contractA);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll((Object[]) null, contractA);
      fail();
    } catch (QuackeryException e) {}
  }

  public void contract_cannot_be_null() {
    suite = suite(name);
    try {
      suite.add(itemA, (Contract<Object>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll(asList(), (Contract<Object>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll(new Object[0], (Contract<Object>) null);
      fail();
    } catch (QuackeryException e) {}
  }
}
