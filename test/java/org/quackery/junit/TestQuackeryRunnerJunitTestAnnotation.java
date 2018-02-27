package org.quackery.junit;

import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitCoreRunner.run;
import static org.quackery.testing.Assertions.assertEquals;

import java.io.IOException;

import org.junit.runner.Result;

public class TestQuackeryRunnerJunitTestAnnotation {
  private final String name = "name";
  private Result result;

  public void method_name_is_used() {
    result = run(new JunitClassBuilder()
        .define(defaultJunitMethod()
            .name(name)
            .throwing(AssertionError.class))
        .load());

    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), name);
  }

  public void test_succeeds_if_nothing_is_thrown() {
    result = run(new JunitClassBuilder()
        .define(defaultJunitMethod()
            .returning(null))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 0);
  }

  public void test_fails_if_throwable_is_thrown() {
    result = run(new JunitClassBuilder()
        .define(defaultJunitMethod()
            .throwing(IOException.class))
        .load());

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getIgnoreCount(), 0);
    assertEquals(result.getFailures().get(0).getException().getClass(), IOException.class);
  }
}
