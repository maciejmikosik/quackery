package org.testanza;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import junit.framework.TestCase;

public class describe_BodyTest {
  private static BodyTester<Object> tester;
  private static Object item = newObject("item");
  private static Object otherItem = newObject("otherItem");
  private static String testName = "testName";
  private static TestCase testCase, otherTestCase;

  public static void test_for_same_item_has_same_name() {
    tester = new BodyTester<Object>() {
      protected String name(Object o) {
        return testName;
      }

      protected void body(Object o) throws Throwable {}
    };
    testCase = (TestCase) tester.test(item);
    otherTestCase = (TestCase) tester.test(item);
    assertEquals(testCase.getName(), otherTestCase.getName());
  }

  public static void test_for_different_item_has_different_name() {
    tester = new BodyTester<Object>() {
      protected String name(Object o) {
        return testName;
      }

      protected void body(Object o) throws Throwable {}
    };
    testCase = (TestCase) tester.test(item);
    otherTestCase = (TestCase) tester.test(otherItem);
    assertNotEquals(testCase.getName(), otherTestCase.getName());
  }

  private static Object newObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }
}
