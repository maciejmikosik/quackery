package org.quackery.junit;

import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.junit.JunitCoreRunner.run;
import static org.quackery.testing.Assertions.assertEquals;

import java.io.IOException;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestQuackeryRunnerInitialization {
  private Result result;
  private final String methodName = "methodName";

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
}
