package org.testanza;

import static org.testanza.Testilities.name;
import static org.testanza.Testilities.newObject;
import static org.testanza.describe_testanza.verify;
import junit.framework.Test;

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
    verify(name(test).equals(testName));
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
    verify(!name(test).equals(name(otherTest)));
  }

  private static abstract class NoBodyTester<T> extends BodyTester<T> {
    protected void body(T t) throws Throwable {}
  }
}
