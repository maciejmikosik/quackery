package org.quackery.contract.collection;

import static java.util.stream.Collectors.toList;
import static org.quackery.Suite.suite;

import org.quackery.Test;

public class Includes {
  public static Test includeIf(boolean condition, Test test) {
    return condition
        ? test
        : suite("");
  }

  public static Test included(Test test) {
    return test.visit(
        (name, body) -> test,
        (name, children) -> suite(name)
            .addAll(children.stream()
                .map(child -> included(child))
                .filter(child -> !isEmptySuite(child))
                .collect(toList())));
  }

  private static boolean isEmptySuite(Test test) {
    return test.visit(
        (name, body) -> false,
        (name, children) -> children.isEmpty());
  }
}
