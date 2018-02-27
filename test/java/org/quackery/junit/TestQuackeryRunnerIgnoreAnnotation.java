package org.quackery.junit;

import static org.quackery.junit.JunitClassBuilder.annotationIgnore;
import static org.quackery.junit.JunitClassBuilder.annotationJunitTest;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.junit.JunitCoreRunner.run;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Mocks.mockCase;

import org.junit.runner.Result;
import org.quackery.report.AssertException;

public class TestQuackeryRunnerIgnoreAnnotation {
  private Result result;

  public void ignore_annotation_on_method_ignores_junit_test() {
    result = run(new JunitClassBuilder()
        .define(defaultJunitMethod()
            .annotations(annotationJunitTest(), annotationIgnore(""))
            .throwing(AssertionError.class))
        .load());

    assertEquals(result.getRunCount(), 0);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 1);
  }

  public void ignore_annotation_on_class_ignores_junit_test() {
    result = run(new JunitClassBuilder()
        .annotate(annotationIgnore(""))
        .define(defaultJunitMethod()
            .throwing(AssertionError.class))
        .load());

    assertEquals(result.getRunCount(), 0);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 1);
  }

  public void ignore_annotation_on_class_ignores_quackery_test() {
    result = run(new JunitClassBuilder()
        .annotate(annotationIgnore(""))
        .define(defaultQuackeryMethod()
            .returning(mockCase("name", new AssertException())))
        .load());

    assertEquals(result.getRunCount(), 0);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 1);
  }
}
