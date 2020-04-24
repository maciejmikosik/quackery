package org.quackery;

import static org.quackery.QuackeryException.check;

import java.util.List;
import java.util.function.BiFunction;

public abstract class Test {
  public final String name;

  protected Test(String name) {
    check(name != null);
    this.name = name;
  }

  public abstract <R> R visit(
      BiFunction<String, Body, R> caseHandler,
      BiFunction<String, List<Test>, R> suiteHandler);
}
