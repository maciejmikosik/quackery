package org.quackery.junit;

import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.quackery.Test;

class JunitCoreRunner {
  public static Result run(Class<?> type) {
    return new JUnitCore().run(type);
  }

  public static Throwable runFailing(Class<?> type) {
    return run(type).getFailures().get(0).getException();
  }

  public static Result run(Test test) {
    return run(new JunitClassBuilder()
        .define(defaultQuackeryMethod().returning(test))
        .load());
  }
}
