package org.quackery;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.quackery.QuackeryException.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Suite implements Test {
  public final String name;
  public final List<Test> children;

  private Suite(String name, List<Test> children) {
    this.name = name;
    this.children = children;
  }

  private static Suite suite(String name, List<Test> children) {
    check(name != null);
    check(children != null);
    Suite suite = new Suite(name, unmodifiableList(new ArrayList<Test>(children)));
    check(!suite.children.contains(null));
    return suite;
  }

  public static Suite suite(String name) {
    check(name != null);
    return suite(name, Arrays.<Test> asList());
  }

  public Suite add(Test newChild) {
    check(newChild != null);
    ArrayList<Test> allChildren = new ArrayList<>();
    allChildren.addAll(children);
    allChildren.add(newChild);
    return suite(name, allChildren);
  }

  public Suite addAll(Iterable<? extends Test> newChildren) {
    check(newChildren != null);
    ArrayList<Test> allChildren = new ArrayList<>();
    allChildren.addAll(children);
    for (Test newChild : newChildren) {
      check(newChild != null);
      allChildren.add(newChild);
    }
    return suite(name, allChildren);
  }

  public Suite addAll(Test[] newChildren) {
    check(newChildren != null);
    return addAll(asList(newChildren));
  }

  public String toString() {
    return name;
  }
}
