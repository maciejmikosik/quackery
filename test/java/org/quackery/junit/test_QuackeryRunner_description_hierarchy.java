package org.quackery.junit;

import static java.util.Arrays.asList;
import static org.quackery.Suite.suite;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Mocks.mockCase;

import java.util.HashSet;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class test_QuackeryRunner_description_hierarchy {
  private Result result;
  private final String testName = "caseName";
  private final String junitMethodName = "junitMethodName";
  private QuackeryRunner runner;
  private Class<?> junitTestClass;

  public void single_quackery_annotated_method_becomes_root() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod().returning(mockCase(testName, new Throwable())))
        .load());

    result = new JUnitCore().run(runner);

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

    assertEquals(result.getFailureCount(), 2);
    assertEquals(new HashSet<>(runner.getDescription().getChildren()), new HashSet<>(asList(
        result.getFailures().get(0).getDescription(),
        result.getFailures().get(1).getDescription())));
    assertEquals(new HashSet(asList(
        result.getFailures().get(0).getDescription().getMethodName(),
        result.getFailures().get(1).getDescription().getMethodName())),
        new HashSet(asList(testName, junitMethodName)));
  }

  public void empty_suite_is_replaced_by_successful_case() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod()
            .returning(suite(testName)))
        .load());

    result = new JUnitCore().run(runner);

    assertEquals(runner.getDescription().getMethodName(), testName);
    assertEquals(result.getFailureCount(), 0);
  }

  public void class_with_no_annotated_methods_is_replaced_by_successful_case() {
    junitTestClass = new JunitClassBuilder().load();
    runner = new QuackeryRunner(junitTestClass);

    result = new JUnitCore().run(runner);

    assertEquals(runner.getDescription().getMethodName(), junitTestClass.getSimpleName());
    assertEquals(result.getFailureCount(), 0);
  }
}
