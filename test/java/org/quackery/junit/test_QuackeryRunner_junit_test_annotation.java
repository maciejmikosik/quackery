package org.quackery.junit;

import static org.quackery.junit.JunitCoreRunner.run;
import static org.quackery.testing.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

public class test_QuackeryRunner_junit_test_annotation {
  private Result result;

  public void method_name_is_used() {
    @RunWith(QuackeryRunner.class)
    class TestClass {
      @Test
      public void test() {
        throw new AssertionError();
      }
    }
    result = run(TestClass.class);

    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), "test");
  }

  public void test_succeeds_if_nothing_is_thrown() {
    @RunWith(QuackeryRunner.class)
    class TestClass {
      @Test
      public void test() {}
    }

    result = run(TestClass.class);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 0);
  }

  public void test_fails_if_throwable_is_thrown() {
    class TestException extends Throwable {}
    @RunWith(QuackeryRunner.class)
    class TestClass {
      @Test
      public void test() throws Throwable {
        throw new TestException();
      }
    }

    result = run(TestClass.class);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    assertEquals(result.getFailures().get(0).getException().getClass(), TestException.class);
  }
}
