package org.quackery.junit;

import static java.lang.String.format;
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

public class TestQuackeryRunnerQuackeryAnnotation {
  private final String name = "name " + hashCode();
  private final String message = "message";
  private final String firstLine = "first line";
  private final String secondLine = "second line";
  private Throwable throwable = new Throwable();
  private Result result;
  private boolean invoked, otherInvoked;
  private Throwable failure;
  private Test test;
  private QuackeryRunner runner;

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

  public void empty_case_name_is_replaced() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(mockCase("")))
        .load());

    assertEquals(
        runner.getDescription().getMethodName(),
        "[empty_name]");
  }

  public void empty_suite_name_is_replaced() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(suite("")
                .add(mockCase(name))
                .add(mockCase(name))))
        .load());

    assertEquals(
        runner.getDescription().getDisplayName(),
        "[empty_name]");
  }

  public void name_with_line_feed_is_escaped() {
    test = mockCase(format("%s\n%s", firstLine, secondLine), new Throwable());

    result = run(test);

    assertEquals(
        result.getFailures().get(0).getDescription().getMethodName(),
        format("%s %s", firstLine, secondLine));
  }

  public void name_with_carriage_return_is_escaped() {
    test = mockCase(format("%s\r%s", firstLine, secondLine), new Throwable());

    result = run(test);

    assertEquals(
        result.getFailures().get(0).getDescription().getMethodName(),
        format("%s %s", firstLine, secondLine));
  }

  public void class_can_have_more_than_one_annotated_method() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .name("testA")
            .returning(mockCase("name")))
        .define(defaultQuackeryMethod()
            .name("testB")
            .returning(mockCase("name")))
        .load());

    assertEquals(result.getRunCount(), 2);
    assertEquals(result.getFailureCount(), 0);
  }

  public void annotated_method_can_return_suite() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returnType(Suite.class)
            .returning(suite("suite")
                .add(mockCase("case"))))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void annotated_method_can_return_case() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returnType(Case.class)
            .returning(mockCase("case")))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void annotated_method_must_be_public() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .modifiers(defaultQuackeryMethod().modifiers & ~PUBLIC)
            .returning(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  public void annotated_method_must_be_static() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .modifiers(defaultQuackeryMethod().modifiers & ~STATIC)
            .returning(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  public void annotated_method_cannot_return_object() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returnType(Object.class)
            .returning(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }

  public void annotated_method_cannot_have_parameters() {
    failure = runFailing(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .parameters(Object.class)
            .returning(mockCase("name")))
        .load());

    assertEquals(failure.getClass(), QuackeryException.class);
  }
}
