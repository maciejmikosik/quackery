package org.quackery;

import java.util.List;
import java.util.function.BiFunction;

public abstract class Test {
  protected Test() {}

  public abstract <R> R visit(
      BiFunction<String, Body, R> caseHandler,
      BiFunction<String, List<Test>, R> suiteHandler);
}
