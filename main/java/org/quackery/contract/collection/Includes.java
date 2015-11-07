package org.quackery.contract.collection;

import static org.quackery.Suite.suite;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Includes {
  public static Test includeIf(boolean condition, Test test) {
    return condition
        ? test
        : suite("");
  }

  public static Test included(Test test) {
    return test instanceof Case
        ? test
        : included((Suite) test);
  }

  private static Test included(Suite suite) {
    List<Test> includedChildren = new ArrayList<>();
    for (Test child : suite.tests) {
      Test includedChild = included(child);
      if (!isEmptySuite(includedChild)) {
        includedChildren.add(includedChild);
      }
    }
    return suite(suite.name).addAll(includedChildren);
  }

  private static boolean isEmptySuite(Test test) {
    return test instanceof Suite && ((Suite) test).tests.isEmpty();
  }
}
