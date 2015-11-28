package org.quackery.junit;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static org.quackery.Suite.suite;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.junit.JunitCoreRunner.run;
import static org.quackery.junit.JunitCoreRunner.runFailing;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Mocks.mockCase;

import org.junit.runner.Result;
import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class test_QuackeryRunner_quackery_annotation {
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
    test = mockCase("anything", throwable);

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
    throwable = new AssertException(message);
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
    throwable = new AssumeException(message);
    test = mockCase(name, throwable);

    result = run(test);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 0);
  }

  public void case_is_invoked_even_if_name_collides() {
    test = suite("suite")
        .add(new Case(name) {
          public void run() {
            invoked = true;
          }
        })
        .add(new Case(name) {
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
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .name("testA")
            .returns(mockCase("name")))
        .define(defaultQuackeryMethod()
            .name("testB")
            .returns(mockCase("name")))
        .load());

    assertEquals(result.getRunCount(), 2);
    assertEquals(result.getFailureCount(), 0);
  }

  public void annotated_method_can_return_suite() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returnType(Suite.class)
            .returns(suite("suite")
                .add(mockCase("case"))))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void annotated_method_can_return_case() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returnType(Case.class)
            .returns(mockCase("case")))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void class_must_have_annotated_method() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .annotations()
            .returns(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  public void annotated_method_must_be_public() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .modifiers(defaultQuackeryMethod().modifiers & ~PUBLIC)
            .returns(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  public void annotated_method_must_be_static() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .modifiers(defaultQuackeryMethod().modifiers & ~STATIC)
            .returns(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  public void annotated_method_cannot_return_object() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returnType(Object.class)
            .returns(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  public void annotated_method_cannot_have_parameters() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .parameters(Object.class)
            .returns(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }
}
