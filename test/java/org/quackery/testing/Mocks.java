package org.quackery.testing;

public class Mocks {
  public static Object mockObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }
}
