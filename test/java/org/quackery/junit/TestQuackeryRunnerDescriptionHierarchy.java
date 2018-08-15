package org.quackery.junit;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.quackery.Suite.suite;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.mockCase;

import java.util.HashSet;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestQuackeryRunnerDescriptionHierarchy {
  private Result result;
  private final String testName = "testName";
  private final String junitMethodName = "junitMethodName";
  private QuackeryRunner runner;
  private Class<?> junitTestClass;

  public void single_quackery_annotated_method_becomes_root() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(mockCase(testName, new Throwable())))
        .load());

    result = new JUnitCore().run(runner);

    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getFailures().get(0).getDescription(), runner.getDescription());
    assertEquals(result.getFailures().get(0).getDescription().getMethodName(), testName);
  }

  public void mixed_quackery_and_junit_annotated_methods_become_children_of_root() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(mockCase(testName, new Throwable())))
        .define(defaultJunitMethod()
            .name(junitMethodName)
            .throwing(Throwable.class))
        .load());

    result = new JUnitCore().run(runner);

    assertEquals(result.getRunCount(), 2);
    assertEquals(result.getFailureCount(), 2);
    assertEquals(
        new HashSet<>(runner.getDescription().getChildren()),
        new HashSet<>(asList(
            result.getFailures().get(0).getDescription(),
            result.getFailures().get(1).getDescription())));
    assertEquals(
        new HashSet<>(asList(
            result.getFailures().get(0).getDescription().getMethodName(),
            result.getFailures().get(1).getDescription().getMethodName())),
        new HashSet<>(asList(testName, junitMethodName)));
  }

  public void empty_suite_is_replaced_by_successful_case() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(suite(testName)))
        .load());

    result = new JUnitCore().run(runner);

    assertEquals(runner.getDescription().getMethodName(), testName);
    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void empty_suite_mixed_with_junit_method_is_replaced_by_successful_case() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(suite(testName)))
        .define(defaultJunitMethod()
            .name(junitMethodName))
        .load());

    result = new JUnitCore().run(runner);

    assertEquals(
        new HashSet<>(asList(
            runner.getDescription().getChildren().get(0).getMethodName(),
            runner.getDescription().getChildren().get(1).getMethodName())),
        new HashSet<>(asList(testName, junitMethodName)));
    assertEquals(result.getRunCount(), 2);
    assertEquals(result.getFailureCount(), 0);
  }

  public void class_with_no_annotated_methods_is_replaced_by_successful_case() {
    junitTestClass = new JunitClassBuilder().load();
    runner = new QuackeryRunner(junitTestClass);

    result = new JUnitCore().run(runner);

    assertEquals(runner.getDescription().getMethodName(), junitTestClass.getSimpleName());
    assertEquals(result.getRunCount(), 1);
    assertEquals(result.getFailureCount(), 0);
  }

  public void display_name_uses_outer_class() {
    junitTestClass = new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(mockCase(testName, new Throwable())))
        .load();
    runner = new QuackeryRunner(junitTestClass);

    assertEquals(
        runner.getDescription().getDisplayName(),
        format("%s(%s)", testName, junitTestClass.getName()));
  }
}
