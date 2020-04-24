package org.quackery.help;

import static org.quackery.Case.newCase;
import static org.quackery.Suite.suite;

import java.util.Optional;

import org.quackery.Body;
import org.quackery.Case;
import org.quackery.Test;

public class Helpers {
  public static Case successfulCase(String name) {
    return new Case(name) {
      public void run() {}
    };
  }

  public static Case failingCase(String name, final Throwable throwable) {
    return new Case(name) {
      public void run() throws Throwable {
        throw throwable;
      }
    };
  }

  public static Test rename(String name, final Test test) {
    return test.visit(
        (oldName, body) -> newCase(name, body),
        (oldName, children) -> suite(name).addAll(children));
  }

  public static Optional<Throwable> thrownBy(Body body) {
    try {
      body.run();
      return Optional.empty();
    } catch (Throwable throwable) {
      return Optional.of(throwable);
    }
  }
}
