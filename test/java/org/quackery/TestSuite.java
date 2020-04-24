package org.quackery;

import static java.util.Arrays.asList;
import static org.quackery.Suite.suite;
import static org.quackery.testing.Testing.assertChildren;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.childrenOf;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;
import static org.quackery.testing.Testing.mockContract;
import static org.quackery.testing.Testing.mockObject;
import static org.quackery.testing.Testing.nameOf;

import java.util.List;

public class TestSuite {
  public static void test_suite() {
    implements_test_interface();
    creates_empty_suite();
    adds_test();
    adds_tests_from_iterable();
    adds_tests_from_array();
    adds_test_produced_by_contract_and_item();
    adds_tests_produced_by_contract_and_items_from_iterable();
    adds_tests_produced_by_contract_and_items_from_array();
    allows_wildcards();
    implements_to_string();
    validates_arguments();
  }

  private static void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Suite.class));
  }

  private static void creates_empty_suite() {
    String name = "name";
    Suite suite = suite(name);

    assertEquals(nameOf(suite), name);
    assertChildren(suite, asList());
  }

  private static void adds_test() {
    Test testA = mockCase("caseA");
    Test testB = mockCase("caseB");
    Test testC = mockCase("caseC");

    Suite suite = suite("suite")
        .add(testA)
        .add(testB)
        .add(testC);

    assertChildren(suite, asList(testA, testB, testC));
  }

  private static void adds_tests_from_iterable() {
    Test testA = mockCase("caseA");
    Test testB = mockCase("caseB");
    Test testC = mockCase("caseC");
    Test testD = mockCase("caseD");

    Suite suite = suite("suite")
        .addAll(asList(testA, testB))
        .addAll(asList(testC, testD));

    assertChildren(suite, asList(testA, testB, testC, testD));
  }

  private static void adds_tests_from_array() {
    Test testA = mockCase("caseA");
    Test testB = mockCase("caseB");
    Test testC = mockCase("caseC");
    Test testD = mockCase("caseD");

    Suite suite = suite("suite")
        .addAll(new Test[] { testA, testB })
        .addAll(new Test[] { testC, testD });

    assertChildren(suite, asList(testA, testB, testC, testD));
  }

  private static void adds_test_produced_by_contract_and_item() {
    Object itemA = mockObject("itemA");
    Object itemB = mockObject("itemB");
    Object itemC = mockObject("itemC");
    Contract<Object> contractA = mockContract("contractA");
    Contract<Object> contractB = mockContract("contractB");
    Contract<Object> contractC = mockContract("contractC");

    Suite suite = suite("suite")
        .add(itemA, contractA)
        .add(itemB, contractB)
        .add(itemC, contractC);

    assertChildren(suite, asList(
        contractA.test(itemA),
        contractB.test(itemB),
        contractC.test(itemC)));
  }

  private static void adds_tests_produced_by_contract_and_items_from_iterable() {
    Object itemA = mockObject("itemA");
    Object itemB = mockObject("itemB");
    Object itemC = mockObject("itemC");
    Object itemD = mockObject("itemD");
    Contract<Object> contractA = mockContract("contractA");
    Contract<Object> contractB = mockContract("contractB");

    Suite suite = suite("suite")
        .addAll(asList(itemA, itemB), contractA)
        .addAll(asList(itemC, itemD), contractB);

    assertChildren(suite, asList(
        contractA.test(itemA),
        contractA.test(itemB),
        contractB.test(itemC),
        contractB.test(itemD)));
  }

  private static void adds_tests_produced_by_contract_and_items_from_array() {
    Object itemA = mockObject("itemA");
    Object itemB = mockObject("itemB");
    Object itemC = mockObject("itemC");
    Object itemD = mockObject("itemD");
    Contract<Object> contractA = mockContract("contractA");
    Contract<Object> contractB = mockContract("contractB");

    Suite suite = suite("suite")
        .addAll(new Object[] { itemA, itemB }, contractA)
        .addAll(new Object[] { itemC, itemD }, contractB);

    assertEquals(childrenOf(suite), asList(
        contractA.test(itemA),
        contractA.test(itemB),
        contractB.test(itemC),
        contractB.test(itemD)));
  }

  private static void allows_wildcards() {
    class Item {}
    class SubItem extends Item {}

    List<Case> cases = asList(mockCase("case"));
    List<Suite> suites = asList(suite("suite"));
    List<Test> tests = asList(suite("suite"), mockCase("case"));
    List<? extends Test> covariantTests = asList(suite("suite"), mockCase("case"));

    List<Item> items = asList(new Item());
    List<SubItem> subItems = asList(new SubItem());
    List<? extends Item> covariantItems = asList(new Item(), new SubItem());

    Contract<Item> contract = mockContract("contract");
    Contract<? super Item> contravariantContract = mockContract("covariantContract");

    suite("suite")
        .addAll(cases)
        .addAll(suites)
        .addAll(tests)
        .addAll(covariantTests)
        .addAll(items, contract)
        .addAll(subItems, contract)
        .addAll(covariantItems, contract)
        .addAll(items, contravariantContract)
        .addAll(subItems, contravariantContract)
        .addAll(covariantItems, contravariantContract);
  }

  private static void implements_to_string() {
    String name = "name";

    Suite suite = suite(name);

    assertEquals(suite.toString(), name);
  }

  private static void validates_arguments() {
    Suite suite = suite("suite");
    Case testA = mockCase("testA");
    Case testB = mockCase("testB");
    Contract<Object> contract = mockContract("contract");
    Object item = mockObject("item");

    try {
      suite(null);
      fail();
    } catch (QuackeryException e) {}

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

    try {
      suite.addAll((Iterable<Object>) null, contract);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll((Object[]) null, contract);
      fail();
    } catch (QuackeryException e) {}

    try {
      suite.add(item, (Contract<Object>) null);
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
