package org.quackery.run;

import static org.quackery.QuackeryException.check;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Reports {
  public static int count(Class<? extends Throwable> type, Test test) {
    check(type != null);
    check(test != null);
    return test instanceof Case
        ? count(type, (Case) test)
        : count(type, (Suite) test);
  }

  private static int count(Class<? extends Throwable> type, Case test) {
    try {
      test.run();
      return 0;
    } catch (Throwable throwable) {
      return type.isInstance(throwable)
          ? 1
          : 0;
    }
  }

  private static int count(Class<? extends Throwable> type, Suite suite) {
    int count = 0;
    for (Test child : suite.tests) {
      count += count(type, child);
    }
    return count;
  }
}
