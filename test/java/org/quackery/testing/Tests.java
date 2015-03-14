package org.quackery.testing;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Tests {
  public static String name(Test test) {
    return test instanceof Case
        ? ((Case) test).name
        : test instanceof Suite
            ? ((Suite) test).name
            : unknown(String.class);
  }

  public static void run(Test test) throws Throwable {
    if (test instanceof Case) {
      ((Case) test).run();
    } else if (test instanceof Suite) {
      for (Test subtest : ((Suite) test).tests) {
        run(subtest);
      }
    } else {
      unknown(test.getClass());
    }
  }

  private static <T> T unknown(Class<T> type) {
    throw new RuntimeException("" + type);
  }
}
