package org.quackery.contract.collection;

import static java.util.stream.Collectors.toList;
import static org.quackery.Suite.suite;
import static org.quackery.help.Helpers.traverseSuites;

import org.quackery.Test;

public class Includes {
  public static Test includeIf(boolean condition, Test test) {
    return condition
        ? test
        : suite("");
  }

  public static Test filterIncluded(Test test) {
    return traverseSuites(test,
        (name, children) -> suite(name)
            .addAll(children.stream()
                .map(child -> filterIncluded(child))
                .filter(child -> !isEmptySuite(child))
                .collect(toList())));
  }

  private static boolean isEmptySuite(Test test) {
    return test.visit(
        (name, body) -> false,
        (name, children) -> children.isEmpty());
  }
}
