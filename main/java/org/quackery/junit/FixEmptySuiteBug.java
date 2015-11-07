package org.quackery.junit;

import static org.quackery.Suite.suite;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class FixEmptySuiteBug {
  public static Test fixEmptySuiteBug(Test test) {
    return test instanceof Suite
        ? fix((Suite) test)
        : test;
  }

  private static Test fix(Suite suite) {
    return suite.tests.isEmpty()
        ? fixEmpty(suite)
        : fixChildren(suite);
  }

  private static Case fixEmpty(Suite suite) {
    return new Case(suite.name) {
      public void run() {}
    };
  }

  private static Test fixChildren(Suite suite) {
    Suite fixedSuite = suite(suite.name);
    for (Test test : suite.tests) {
      fixedSuite = fixedSuite.add(fixEmptySuiteBug(test));
    }
    return fixedSuite;
  }
}
