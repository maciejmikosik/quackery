package org.quackery.junit;

import static org.quackery.junit.JunitClassBuilder.annotationIgnore;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.junit.JunitCoreRunner.run;
import static org.quackery.testing.Assertions.assertEquals;

import org.junit.runner.Result;
import org.quackery.testing.Mocks;

public class test_QuackeryRunner_mixed_annotations {
  private Result result;

  public void runs_mixed_tests() {
    result = run(new JunitClassBuilder()
        .define(defaultJunitMethod()
            .name("junit_test")
            .returning(null))
        .define(JunitClassBuilder.defaultQuackeryMethod()
            .name("quackery_test")
            .returning(Mocks.mockCase("quackery_case")))
        .load());

    assertEquals(result.getRunCount(), 2);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 0);
  }

  public void ignore_annotation_on_class_ignores_quackery_test() {
    result = run(new JunitClassBuilder()
        .annotate(annotationIgnore(""))
        .define(defaultQuackeryMethod()
            .throwing(AssertionError.class))
        .load());

    assertEquals(result.getRunCount(), 0);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 1);
  }
}
