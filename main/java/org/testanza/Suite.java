package org.testanza;

import static java.util.Collections.unmodifiableList;
import static org.testanza.TestanzaException.check;

import java.util.ArrayList;
import java.util.List;

public class Suite implements Test {
  public final String name;
  public final List<Test> tests;

  private Suite(String name, List<Test> tests) {
    this.name = name;
    this.tests = tests;
  }

  public static Suite newSuite(String name, List<? extends Test> tests) {
    check(name != null);
    check(tests != null);
    return verify(new Suite(name, immutable((List<Test>) tests)));
  }

  private static Suite verify(Suite suite) {
    check(!suite.tests.contains(null));
    return suite;
  }

  private static <E> List<E> immutable(List<E> list) {
    return unmodifiableList(new ArrayList<E>(list));
  }
}
