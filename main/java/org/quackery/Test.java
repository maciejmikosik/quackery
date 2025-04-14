package org.quackery;

import java.util.List;
import java.util.function.BiFunction;

public sealed interface Test permits Case, Suite {
  <R> R visit(
      BiFunction<String, Body, R> caseHandler,
      BiFunction<String, List<Test>, R> suiteHandler);
}
