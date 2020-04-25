package org.quackery.report;

import static org.quackery.QuackeryException.check;
import static org.quackery.help.Helpers.thrownBy;

import org.quackery.Body;
import org.quackery.Test;

public class Reports {
  public static int count(Class<? extends Throwable> type, Test test) {
    check(type != null);
    check(test != null);
    return test.visit(
        (name, body) -> thrownBy(body)
            .map(throwable -> type.isInstance(throwable) ? 1 : 0)
            .orElse(0),
        (name, children) -> children.stream()
            .mapToInt(child -> count(type, child))
            .sum());
  }

  public static String format(Test test) {
    check(test != null);
    return append(0, test, new StringBuilder()).toString();
  }

  private static StringBuilder append(int indentation, Test test, StringBuilder builder) {
    return test.visit(
        (name, body) -> {
          indent(indentation, builder);
          appendThrowable(body, builder);
          return builder.append(name).append("\n");
        },
        (name, children) -> {
          indent(indentation, builder);
          builder.append(name).append("\n");
          children.forEach(child -> append(indentation + 1, child, builder));
          return builder;
        });
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
