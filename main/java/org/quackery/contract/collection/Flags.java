package org.quackery.contract.collection;

import static org.quackery.Suite.suite;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Flags {
  public static Test onlyIf(boolean condition, Test test) {
    return condition
        ? test
        : suite("");
  }

  public static Test clean(Test test) {
    return test instanceof Case
        ? test
        : clean((Suite) test);
  }

  private static Test clean(Suite suite) {
    List<Test> cleanedChildren = new ArrayList<>();
    for (Test child : suite.tests) {
      Test cleanChild = clean(child);
      if (!isEmptySuite(cleanChild)) {
        cleanedChildren.add(cleanChild);
      }
    }
    return suite(suite.name).testAll(cleanedChildren);
  }

  private static boolean isEmptySuite(Test test) {
    return test instanceof Suite && ((Suite) test).tests.isEmpty();
  }
}
