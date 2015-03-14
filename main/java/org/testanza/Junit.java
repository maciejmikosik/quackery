package org.testanza;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Junit {
  public static junit.framework.Test junit(Test test) {
    return test instanceof Case
        ? junit((Case) test)
        : junit((Suite) test);
  }

  private static TestSuite junit(Suite suite) {
    TestSuite testSuite = new TestSuite(suite.name);
    for (Test test : suite.tests) {
      testSuite.addTest(junit(test));
    }
    return testSuite;
  }

  private static TestCase junit(final Case cas) {
    return makeNameUnique(new TestCase(cas.name) {
      protected void runTest() throws Throwable {
        try {
          cas.run();
        } catch (TestanzaAssertionException e) {
          throw new AssertionError(e.getMessage(), e);
        }
      }
    });
  }

  private static synchronized TestCase makeNameUnique(TestCase test) {
    String name = test.getName();
    String newName = name;
    if (names.containsKey(name) && !test.equals(names.get(name).get())) {
      for (int i = 1;; i++) {
        newName = name + " #" + i;
        if (!names.containsKey(newName)) {
          break;
        }
      }

    }
    names.put(newName, new WeakReference<TestCase>(test));
    test.setName(newName);
    return test;
  }

  private static final Map<String, WeakReference<TestCase>> names = new WeakHashMap<String, WeakReference<TestCase>>();
}
