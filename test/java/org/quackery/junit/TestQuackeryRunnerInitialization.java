package org.quackery.junit;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.junit.JunitCoreRunner.run;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Mocks.mockCase;

import java.io.IOException;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestQuackeryRunnerInitialization {
  private Result result;
  private final String methodName = "methodName";
  private final String caseName = "caseName";

  public void reports_exception_from_quackery_annotated_method() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .name(methodName)
            .throwing(IOException.class))
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(), methodName);
    assertEquals(failure.getException().getClass(), IOException.class);
  }

  public void reports_quackery_annotated_method_being_not_public() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .name(methodName)
            .modifiers(defaultQuackeryMethod().modifiers & ~PUBLIC)
            .returning(mockCase(caseName)))
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(), methodName);
    assertEquals(failure.getException().getClass(), QuackeryException.class);
    assertEquals(failure.getException().getMessage(), "method must be public");
  }

  public void reports_quackery_annotated_method_being_not_static() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .name(methodName)
            .modifiers(defaultQuackeryMethod().modifiers & ~STATIC)
            .returning(mockCase(caseName)))
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(), methodName);
    assertEquals(failure.getException().getClass(), QuackeryException.class);
    assertEquals(failure.getException().getMessage(), "method must be static");
  }

  public void reports_quackery_annotated_method_wrong_return_type() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .name(methodName)
            .returnType(Object.class)
            .returning(mockCase(caseName)))
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(), methodName);
    assertEquals(failure.getException().getClass(), QuackeryException.class);
    assertEquals(failure.getException().getMessage(),
        "method return type must be assignable to " + Test.class.getName());
  }

  public void reports_quackery_annotated_method_having_parameters() {
    result = run(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .name(methodName)
            .parameters(Object.class)
            .returning(mockCase(caseName)))
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(), methodName);
    assertEquals(failure.getException().getClass(), QuackeryException.class);
    assertEquals(failure.getException().getMessage(), "method cannot have parameters");
  }
}
