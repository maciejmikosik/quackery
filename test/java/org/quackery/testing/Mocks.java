package org.quackery.testing;

import org.quackery.Case;

public class Mocks {
  public static Object mockObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }

  public static Case mockCase(String name) {
    return new Case(name) {
      public void run() {}
    };
  }

  public static Case mockCase(String name, final Throwable throwable) {
    return new Case(name) {
      public void run() throws Throwable {
        throw throwable.fillInStackTrace();
      }
    };
  }
}
