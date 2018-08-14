package org.quackery.junit;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.NO_CONSTRUCTORS;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
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

  public void reports_missing_default_constructor() {
    result = run(new JunitClassBuilder(NO_CONSTRUCTORS)
        .define(defaultJunitMethod())
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(),
        "Test class should have exactly one public constructor");
    assertEquals(failure.getException().getClass(), Exception.class);
    assertEquals(failure.getException().getMessage(),
        "Test class should have exactly one public constructor");
  }

  public void ignores_missing_default_constructor_if_no_junit_test_methods() {
    result = run(new JunitClassBuilder(NO_CONSTRUCTORS)
        .define(defaultQuackeryMethod()
            .returning(mockCase(caseName)))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void reports_default_constructor_being_private() {
    result = run(new JunitClassBuilder(NO_CONSTRUCTORS)
        .defineConstructor(new MethodDefinition()
            .modifiers(PRIVATE)
            .parameters())
        .define(defaultJunitMethod())
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(),
        "Test class should have exactly one public constructor");
    assertEquals(failure.getException().getClass(), Exception.class);
    assertEquals(failure.getException().getMessage(),
        "Test class should have exactly one public constructor");
  }

  public void ignores_default_constructor_being_private_if_no_junit_test_methods() {
    result = run(new JunitClassBuilder(NO_CONSTRUCTORS)
        .defineConstructor(new MethodDefinition()
            .modifiers(PRIVATE)
            .parameters())
        .define(defaultQuackeryMethod()
            .returning(mockCase(caseName)))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void reports_constructor_having_parameters() {
    result = run(new JunitClassBuilder(NO_CONSTRUCTORS)
        .defineConstructor(new MethodDefinition()
            .modifiers(PUBLIC)
            .parameters(Object.class))
        .define(defaultJunitMethod())
        .load());

    assertEquals(result.getFailureCount(), 1);
    Failure failure = result.getFailures().get(0);
    assertEquals(failure.getDescription().getMethodName(),
        "Test class should have exactly one public zero-argument constructor");
    assertEquals(failure.getException().getClass(), Exception.class);
    assertEquals(failure.getException().getMessage(),
        "Test class should have exactly one public zero-argument constructor");
  }

  public void ignores_default_constructor_having_parameters_if_no_junit_test_methods() {
    result = run(new JunitClassBuilder(NO_CONSTRUCTORS)
        .defineConstructor(new MethodDefinition()
            .modifiers(PUBLIC)
            .parameters(Object.class))
        .define(defaultQuackeryMethod()
            .returning(mockCase(caseName)))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }
}
