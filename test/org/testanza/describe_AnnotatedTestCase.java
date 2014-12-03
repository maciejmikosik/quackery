package org.testanza;

import static org.testanza.describe_testanza.verify;
import junit.framework.Test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class describe_AnnotatedTestCase {
  private static boolean wasRan = false;
  private static Result result;
  private static Test test;

  public static void is_runnable_with_one_annotated_method() {
    wasRan = false;
    test = new AnnotatedTestCase("name") {
      @org.junit.Test
      public void test() {
        wasRan = true;
      }
    };
    new JUnitCore().run(test);
    verify(wasRan);
  }

  public static void fails_for_more_than_one_test_method() {
    wasRan = false;
    test = new AnnotatedTestCase("name") {
      @org.junit.Test
      public void test() {
        wasRan = true;
      }

      @org.junit.Test
      public void test2() {
        wasRan = true;
      }
    };
    result = new JUnitCore().run(test);
    verify(!wasRan);
    verify(1 == result.getFailureCount());
  }

  public static void fails_for_no_test_methods() {
    test = new AnnotatedTestCase("name") {};
    result = new JUnitCore().run(test);
    verify(1 == result.getFailureCount());
  }
}
