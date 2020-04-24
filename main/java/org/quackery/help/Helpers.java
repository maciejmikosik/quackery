package org.quackery.help;

import static java.util.stream.Collectors.toList;
import static org.quackery.Case.newCase;
import static org.quackery.Suite.suite;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.quackery.Body;
import org.quackery.Case;
import org.quackery.Test;

public class Helpers {
  public static Case successfulCase(String name) {
    return newCase(name, () -> {});
  }

  public static Case failingCase(String name, Throwable throwable) {
    return newCase(name, () -> {
      throw throwable;
    });
  }

  public static Optional<Throwable> thrownBy(Body body) {
    try {
      body.run();
      return Optional.empty();
    } catch (Throwable throwable) {
      return Optional.of(throwable);
    }
  }

  public static Test traverse(Test test, Function<Test, Test> handler) {
    return test.visit(
        (name, body) -> handler.apply(test),
        (name, children) -> handler.apply(suite(name)
            .addAll(children.stream()
                .map(child -> traverse(child, handler))
                .collect(toList()))));
  }

  public static Test traverseCases(Test root, BiFunction<String, Body, Test> handler) {
    return traverse(root,
        test -> test.visit(
            handler,
            (name, children) -> test));
  }

  public static Test traverseSuites(Test root, BiFunction<String, List<Test>, Test> handler) {
    return traverse(root,
        test -> test.visit(
            (name, body) -> test,
            handler));
  }

  public static Test traverseNames(Test root, Function<String, String> renamer) {
    return traverse(root,
        test -> test.visit(
            (name, body) -> newCase(renamer.apply(name), body),
            (name, children) -> suite(renamer.apply(name)).addAll(children)));
  }

  public static Test traverseBodies(Test root, Function<Body, Body> handler) {
    return traverseCases(root,
        (name, body) -> newCase(name, handler.apply(body)));
  }
}
