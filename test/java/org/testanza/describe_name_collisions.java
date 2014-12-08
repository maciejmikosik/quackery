package org.testanza;

import static org.testanza.Testilities.name;
import static org.testanza.Testilities.newObject;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyNotEquals;
import junit.framework.Test;

public class describe_name_collisions {
  private String testName = "testName";
  private final Object item = newObject("item");
  private final Object otherItem = newObject("otherItem");
  private Tester<Object> tester;
  private Test test, otherTest;

  public void uses_original_name_if_no_collision() {
    testName = "uses_original_name_if_no_collision";
    tester = new NoBodyTester<Object>() {
      protected String name(Object i) {
        return testName;
      }
    };
    test = tester.test(item);
    verifyEquals(name(test), testName);
  }

  public void fixes_colliding_name() {
    testName = "fixes_colliding_name";
    tester = new NoBodyTester<Object>() {
      protected String name(Object i) {
        return testName;
      }
    };
    test = tester.test(item);
    otherTest = tester.test(otherItem);
    verifyNotEquals(name(test), name(otherTest));
  }

  private static abstract class NoBodyTester<T> extends BodyTester<T> {
    protected void body(T t) throws Throwable {}
  }
}
