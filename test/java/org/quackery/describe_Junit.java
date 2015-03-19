package org.quackery;

import static org.quackery.Junit.junit;
import static org.quackery.Suite.newSuite;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertNotEquals;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

public class describe_Junit {
  private final String name = "name " + hashCode();
  private final String message = "message";
  private RuntimeException exception = new RuntimeException("exception");
  private Result result, otherResult;
  private boolean invoked, otherInvoked;
  private Throwable failure;
  private Test test, otherTest;

  public void case_name_is_preserved() {
    test = new Case(name) {
      public void run() throws Throwable {
        throw exception.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), name);
  }

  public void suite_name_is_preserved() {
    test = newSuite(name) //
        .test(new Case("anything") {
          public void run() throws Throwable {
            throw exception.fillInStackTrace();
          }
        });

    result = run(test);

    // TODO assert suite name
  }

  public void case_succeeds_if_no_exception_is_thrown() {
    test = new Case(name) {
      public void run() {
        invoked = true;
      }
    };

    result = run(test);

    assertEquals(invoked, true);
    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 0);
  }

  public void case_fails_if_exception_is_thrown() {
    test = new Case(name) {
      public void run() throws Throwable {
        throw exception.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    failure = result.getFailures().get(0).getException();
    assertEquals(failure, exception);
  }

  public void case_fails_if_quackery_assertion_exception_is_thrown() {
    exception = new QuackeryAssertionException(message);
    test = new Case(name) {
      public void run() throws Throwable {
        throw exception.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    failure = result.getFailures().get(0).getException();
    assertEquals(failure.getClass(), AssertionError.class);
    assertEquals(failure.getMessage(), message);
    assertEquals(failure.getCause(), exception);
  }

  public void case_fails_if_quackery_assumption_exception_is_thrown() {
    exception = new QuackeryAssumptionException(message);
    test = new Case(name) {
      public void run() throws Throwable {
        throw exception.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    failure = result.getFailures().get(0).getException();
    assertEquals(failure.getClass(), AssumptionViolatedException.class);
    assertEquals(failure.getMessage(), message);
    assertEquals(failure.getCause(), exception);
  }

  public void case_name_is_changed_if_collides_across_suite() {
    test = newSuite("suite") //
        .test(new Case(name) {
          public void run() throws Throwable {
            throw exception.fillInStackTrace();
          }
        }) //
        .test(new Case(name) {
          public void run() throws Throwable {
            throw exception.fillInStackTrace();
          }
        });

    result = run(test);

    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), name);
    assertNotEquals(result.getFailures().get(1).getDescription().getMethodName(), name);
  }

  public void case_name_is_changed_if_collides_across_jvm() {
    test = new Case(name) {
      public void run() throws Throwable {
        throw exception.fillInStackTrace();
      }
    };
    otherTest = new Case(name) {
      public void run() throws Throwable {
        throw exception.fillInStackTrace();
      }
    };

    result = run(test);
    otherResult = run(otherTest);

    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), name);
    assertNotEquals(otherResult.getFailures().get(0).getDescription().getMethodName(), name);
  }

  public void case_is_invoked_even_if_name_collides() {
    test = newSuite("suite") //
        .test(new Case(name) {
          public void run() {
            invoked = true;
          }
        }) //
        .test(new Case(name) {
          public void run() {
            otherInvoked = true;
          }
        });

    run(test);

    assertEquals(invoked, true);
    assertEquals(otherInvoked, true);
  }

  private static Result run(Test test) {
    RunnableClass.delegate.set(junit(test));
    return new JUnitCore().run(RunnableClass.class);
  }

  @RunWith(AllTests.class)
  public static class RunnableClass {
    private static final ThreadLocal<junit.framework.Test> delegate = new ThreadLocal<>();

    public static junit.framework.Test suite() {
      return delegate.get();
    }
  }
}
