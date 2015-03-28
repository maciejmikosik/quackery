package org.quackery;

import static org.quackery.Suite.suite;
import static org.quackery.testing.Assertions.assertEquals;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

public class test_QuackeryRunner {
  private final String name = "name " + hashCode();
  private final String message = "message";
  private Throwable throwable = new RuntimeException("exception");
  private Result result;
  private boolean invoked, otherInvoked;
  private Throwable failure;
  private Test test;

  public void case_name_is_preserved() {
    test = new Case(name) {
      public void run() throws Throwable {
        throw throwable.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), name);
  }

  public void suite_name_is_preserved() {
    test = suite(name)
        .test(new Case("anything") {
          public void run() throws Throwable {
            throw throwable.fillInStackTrace();
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
        throw throwable.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    failure = result.getFailures().get(0).getException();
    assertEquals(failure, throwable);
  }

  public void case_fails_if_quackery_assertion_exception_is_thrown() {
    throwable = new AssertionException(message);
    test = new Case(name) {
      public void run() throws Throwable {
        throw throwable.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    failure = result.getFailures().get(0).getException();
    assertEquals(failure.getClass(), AssertionError.class);
    assertEquals(failure.getMessage(), message);
    assertEquals(failure.getCause(), throwable);
  }

  public void case_is_skipped_if_quackery_assumption_exception_is_thrown() {
    throwable = new AssumptionException(message);
    test = new Case(name) {
      public void run() throws Throwable {
        throw throwable.fillInStackTrace();
      }
    };

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 0);
  }

  public void case_is_invoked_even_if_name_collides() {
    test = suite("suite")
        .test(new Case(name) {
          public void run() {
            invoked = true;
          }
        })
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
    RunnableClass.delegate.set(test);
    return new JUnitCore().run(RunnableClass.class);
  }

  @RunWith(QuackeryRunner.class)
  public static class RunnableClass {
    private static final ThreadLocal<Test> delegate = new ThreadLocal<>();

    @Quackery
    public static Test suite() {
      return delegate.get();
    }
  }
}
