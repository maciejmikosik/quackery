package org.quackery;

import java.util.List;
import java.util.function.BiFunction;

public interface Test {
  <R> R visit(
      BiFunction<String, Body, R> caseHandler,
      BiFunction<String, List<Test>, R> suiteHandler);
}
