package org.quackery;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.quackery.QuackeryException.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Suite extends Test {
  public final List<Test> tests;

  private Suite(String name, List<Test> tests) {
    super(name);
    this.tests = tests;
  }

  private static Suite suite(String name, List<Test> tests) {
    check(name != null);
    check(tests != null);
    Suite suite = new Suite(name, unmodifiableList(new ArrayList<Test>(tests)));
    check(!suite.tests.contains(null));
    return suite;
  }

  public static Suite suite(String name) {
    check(name != null);
    return suite(name, Arrays.<Test> asList());
  }

  public Suite add(Test extraTest) {
    check(extraTest != null);
    ArrayList<Test> allTests = new ArrayList<>();
    allTests.addAll(tests);
    allTests.add(extraTest);
    return suite(name, allTests);
  }

  public Suite addAll(Iterable<? extends Test> extraTests) {
    check(extraTests != null);
    ArrayList<Test> allTests = new ArrayList<>();
    allTests.addAll(tests);
    for (Test extraTest : extraTests) {
      check(extraTest != null);
      allTests.add(extraTest);
    }
    return suite(name, allTests);
  }

  public Suite addAll(Test[] extraTests) {
    check(extraTests != null);
    return addAll(asList(extraTests));
  }

  public <T> Suite add(T item, Contract<T> contract) {
    check(contract != null);
    return add(contract.test(item));
  }

  public <T> Suite addAll(Iterable<? extends T> items, Contract<T> contract) {
    check(items != null);
    check(contract != null);
    List<Test> extraTests = new ArrayList<>();
    for (T item : items) {
      extraTests.add(contract.test(item));
    }
    return addAll(extraTests);
  }

  public <T> Suite addAll(T[] items, Contract<T> contract) {
    check(items != null);
    return addAll(asList(items), contract);
  }

  public String toString() {
    return name;
  }
}
