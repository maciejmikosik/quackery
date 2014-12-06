package org.testanza;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Testilities {
  public static Object newObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }

  public static String name(Test test) {
    return test instanceof TestCase
        ? ((TestCase) test).getName()
        : test instanceof TestSuite
            ? ((TestSuite) test).getName()
            : unknown(String.class);
  }

  private static <T> T unknown(Class<T> type) {
    throw new RuntimeException("" + type);
  }
}
