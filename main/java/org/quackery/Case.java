package org.quackery;

import static org.quackery.QuackeryException.check;

import java.util.List;
import java.util.function.BiFunction;

public abstract class Case extends Test {
  protected Case(String name) {
    super(name);
  }

  public abstract void run() throws Throwable;

  public static Case newCase(String name, final Body body) {
    check(body != null);
    return new Case(name) {
      public void run() throws Throwable {
        body.run();
      }
    };
  }

  public <R> R visit(
      BiFunction<String, Body, R> caseHandler,
      BiFunction<String, List<Test>, R> suiteHandler) {
    return caseHandler.apply(name, () -> run());
  }
}
