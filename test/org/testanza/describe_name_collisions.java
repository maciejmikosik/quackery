package org.testanza;

import static org.testanza.describe_testanza.verify;
import junit.framework.Test;
import junit.framework.TestCase;

public class describe_name_collisions {
  private static String testName = "testName";
  private static Object item = newObject("item");
  private static Object otherItem = newObject("otherItem");
  private static Tester<Object> tester;
  private static Test test, otherTest;

  public static void uses_original_name_if_no_collision() {
    testName = "uses_original_name_if_no_collision";
    tester = new NoBodyTester<Object>() {
      protected String name(Object i) {
        return testName;
      }
    };
    test = tester.test(item);
    verify(nameOf(test).equals(testName));
  }

  public static void fixes_colliding_name() {
    testName = "fixes_colliding_name";
    tester = new NoBodyTester<Object>() {
      protected String name(Object i) {
        return testName;
      }
    };
    test = tester.test(item);
    otherTest = tester.test(otherItem);
    verify(!nameOf(test).equals(nameOf(otherTest)));
  }

  private static String nameOf(Test testCase) {
    return ((TestCase) testCase).getName();
  }

  private static Object newObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }

  private static abstract class NoBodyTester<T> extends BodyTester<T> {
    protected void body(T t) throws Throwable {}
  }
}
