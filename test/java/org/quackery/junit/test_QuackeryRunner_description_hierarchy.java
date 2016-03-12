package org.quackery.junit;

import static java.util.Arrays.asList;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Mocks.mockCase;

import java.util.HashSet;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class test_QuackeryRunner_description_hierarchy {
  private Result result;
  private final String name = "name";
  private QuackeryRunner runner;

  public void single_quackery_annotated_method_becomes_root() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod().returning(mockCase(name, new Throwable())))
        .load());

    result = new JUnitCore().run(runner);

    assertEquals(result.getFailureCount(), 1);
    assertEquals(result.getFailures().get(0).getDescription(), runner.getDescription());
  }

  public void mixed_quackery_and_junit_annotated_methods_become_children_of_root() {
    runner = new QuackeryRunner(new JunitClassBuilder()
        .define(defaultQuackeryMethod().returning(mockCase(name, new Throwable())))
        .define(defaultJunitMethod().throwing(Throwable.class))
        .load());

    result = new JUnitCore().run(runner);

    assertEquals(result.getFailureCount(), 2);
    assertEquals(new HashSet<>(runner.getDescription().getChildren()), new HashSet<>(asList(
        result.getFailures().get(0).getDescription(),
        result.getFailures().get(1).getDescription())));
  }
}
