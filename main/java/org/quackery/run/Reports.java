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

  public static String print(Test test) {
    check(test != null);
    return append(0, test, new StringBuilder()).toString();
  }

  private static StringBuilder append(int indentation, Test test, StringBuilder builder) {
    return test instanceof Case
        ? append(indentation, (Case) test, builder)
        : append(indentation, (Suite) test, builder);
  }

  private static StringBuilder append(int indentation, Case cas, StringBuilder builder) {
    indent(indentation, builder);
    appendThrowable(cas, builder);
    return builder.append(cas.name).append("\n");
  }

  private static StringBuilder append(int indentation, Suite suite, StringBuilder builder) {
    indent(indentation, builder);
    builder.append(suite.name).append("\n");
    for (Test test : suite.tests) {
      append(indentation + 1, test, builder);
    }
    return builder;
  }

  private static StringBuilder appendThrowable(Case cas, StringBuilder builder) {
    try {
      cas.run();
    } catch (Throwable e) {
      builder.append("[").append(e.getClass().getSimpleName()).append("] ");
    }
    return builder;
  }

  private static StringBuilder indent(int size, StringBuilder builder) {
    for (int i = 0; i < size; i++) {
      builder.append("  ");
    }
    return builder;
  }
}
