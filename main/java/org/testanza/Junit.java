package org.testanza;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Junit {
  public static junit.framework.Test junit(Test expectation) {
    return expectation instanceof Case
        ? junit((Case) expectation)
        : junit((Suite) expectation);
  }

  private static TestSuite junit(Suite suite) {
    TestSuite testSuite = new TestSuite(suite.name);
    for (Test test : suite.tests) {
      testSuite.addTest(junit(test));
    }
    return testSuite;
  }

  private static TestCase junit(final Case cas) {
    return new TestCase(cas.name) {
      protected void runTest() throws Throwable {
        cas.body.invoke();
      }
    };
  }
}
