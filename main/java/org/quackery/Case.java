package org.quackery;

import static org.quackery.QuackeryException.check;

import java.util.List;
import java.util.function.BiFunction;

public class Case extends Test {
  private final String name;
  private final Body body;

  private Case(String name, Body body) {
    this.name = name;
    this.body = body;
  }

  public static Test newCase(String name, Body body) {
    check(name != null);
    check(body != null);
    return new Case(name, body);
  }

  public <R> R visit(
      BiFunction<String, Body, R> caseHandler,
      BiFunction<String, List<Test>, R> suiteHandler) {
    return caseHandler.apply(name, body);
  }
}
