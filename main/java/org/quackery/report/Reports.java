package org.quackery.report;

import static org.quackery.QuackeryException.check;
import static org.quackery.help.Helpers.thrownBy;

import org.quackery.Script;
import org.quackery.Story;
import org.quackery.Suite;
import org.quackery.Test;

public class Reports {
  public static int count(Class<? extends Throwable> type, Test test) {
    check(type != null);
    check(test != null);
    return switch (test) {
      case Story story -> thrownBy(story.script)
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
      case Story story -> {
        indent(indentation, builder);
        appendThrowable(story.script, builder);
        yield builder.append(story.name).append("\n");
      }
      case Suite suite -> {
        indent(indentation, builder);
        builder.append(suite.name).append("\n");
        suite.children.forEach(child -> append(indentation + 1, child, builder));
        yield builder;
      }
    };
  }

  private static StringBuilder appendThrowable(Script script, StringBuilder builder) {
    return thrownBy(script)
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
