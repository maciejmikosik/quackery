package org.quackery.report;

import static org.quackery.QuackeryException.check;
import static org.quackery.help.Helpers.thrownBy;

import org.quackery.Body;
import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Reports {
  public static int count(Class<? extends Throwable> type, Test test) {
    check(type != null);
    check(test != null);
    return switch (test) {
      case Case cas -> thrownBy(cas.body)
          .map(throwable -> type.isInstance(throwable) ? 1 : 0)
          .orElse(0);
      case Suite suite -> suite.children.stream()
          .mapToInt(child -> count(type, child))
          .sum();
    };
  }

  public static String format(Test test) {
    check(test != null);
    return append(0, test, new StringBuilder()).toString();
  }

  private static StringBuilder append(int indentation, Test test, StringBuilder builder) {
    return switch (test) {
      case Case cas -> {
        indent(indentation, builder);
        appendThrowable(cas.body, builder);
        yield builder.append(cas.name).append("\n");
      }
      case Suite suite -> {
        indent(indentation, builder);
        builder.append(suite.name).append("\n");
        suite.children.forEach(child -> append(indentation + 1, child, builder));
        yield builder;
      }
    };
  }

  private static StringBuilder appendThrowable(Body body, StringBuilder builder) {
    return thrownBy(body)
        .map(throwable -> builder
            .append("[")
            .append(throwable.getClass().getSimpleName())
            .append("] "))
        .orElse(builder);
  }

  private static StringBuilder indent(int size, StringBuilder builder) {
    for (int i = 0; i < size; i++) {
      builder.append("  ");
    }
    return builder;
  }
}
