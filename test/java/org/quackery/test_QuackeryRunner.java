package org.quackery;

import static org.quackery.Suite.suite;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Mocks.mockCase;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

public class test_QuackeryRunner {
  private final String name = "name " + hashCode();
  private final String message = "message";
  private Throwable throwable = new Throwable();
  private Result result;
  private boolean invoked, otherInvoked;
  private Throwable failure;
  private Test test;

  public void case_name_is_preserved() {
    test = mockCase(name, throwable);

    result = run(test);

    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), name);
  }

  public void suite_name_is_preserved() {
    test = suite(name)
        .test(mockCase("anything", throwable));

    result = run(test);

    // TODO assert suite name
  }

  public void case_is_run() {
    test = new Case(name) {
      public void run() {
        invoked = true;
      }
    };

    result = run(test);

    assertEquals(invoked, true);
  }

  public void case_succeeds_if_nothing_is_thrown() {
    test = mockCase(name);

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 0);
  }

  public void case_fails_if_throwable_is_thrown() {
    test = mockCase(name, throwable);

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    failure = result.getFailures().get(0).getException();
    assertEquals(failure, throwable);
  }

  public void case_fails_if_quackery_assertion_exception_is_thrown() {
    throwable = new AssertionException(message);
    test = mockCase(name, throwable);

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
    test = mockCase(name, throwable);

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

  public void empty_suite_does_not_confuse_runner() {
    test = suite("suite");

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void class_can_have_more_than_one_annotated_method() {
    result = run(TwoAnnotatedMethods.class);

    assertEquals(result.getRunCount(), 2);
    assertEquals(result.getFailureCount(), 0);
  }

  @RunWith(QuackeryRunner.class)
  public static class TwoAnnotatedMethods {
    @Quackery
    public static Test testA() {
      return mockCase("name");
    }

    @Quackery
    public static Test testB() {
      return mockCase("name");
    }
  }

  public void annotated_method_can_return_suite() {
    result = run(ReturnsSuite.class);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  @RunWith(QuackeryRunner.class)
  public static class ReturnsSuite {
    @Quackery
    public static Suite test() {
      return suite("suite")
          .test(mockCase("case"));
    }
  }

  public void annotated_method_can_return_case() {
    result = run(ReturnsCase.class);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  @RunWith(QuackeryRunner.class)
  public static class ReturnsCase {
    @Quackery
    public static Case test() {
      return mockCase("case");
    }
  }

  public void class_must_have_annotated_method() {
    failure = runFailing(NoAnnotatedMethods.class);

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  @RunWith(QuackeryRunner.class)
  public static class NoAnnotatedMethods {
    public static Test test() {
      return mockCase("name");
    }
  }

  public void annotated_method_must_be_public() {
    failure = runFailing(NotPublic.class);

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  @RunWith(QuackeryRunner.class)
  public static class NotPublic {
    @Quackery
    static Test test() {
      return mockCase("name");
    }
  }

  public void annotated_method_must_be_static() {
    failure = runFailing(NotStatic.class);

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  @RunWith(QuackeryRunner.class)
  public static class NotStatic {
    @Quackery
    public Test test() {
      return mockCase("name");
    }
  }

  public void annotated_method_must_return_test() {
    failure = runFailing(NotReturnsTest.class);

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  @RunWith(QuackeryRunner.class)
  public static class NotReturnsTest {
    @Quackery
    public static Object suite() {
      return mockCase("name");
    }
  }

  public void annotated_method_cannot_have_parameters() {
    failure = runFailing(HasParameters.class);

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  @RunWith(QuackeryRunner.class)
  public static class HasParameters {
    @Quackery
    public static Test test(Object parameter) {
      return mockCase("name");
    }
  }

  private static Result run(Class<?> type) {
    return new JUnitCore().run(type);
  }

  private static Throwable runFailing(Class<?> type) {
    return run(type).getFailures().get(0).getException();
  }

  private static Result run(Test test) {
    RunnableClass.localTest.set(test);
    return run(RunnableClass.class);
  }

  @RunWith(QuackeryRunner.class)
  public static class RunnableClass {
    private static final ThreadLocal<Test> localTest = new ThreadLocal<>();

    @Quackery
    public static Test test() {
      return localTest.get();
    }
  }
}
