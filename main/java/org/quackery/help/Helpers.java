package org.quackery.help;

import static org.quackery.Case.newCase;

import java.util.Optional;

import org.quackery.Script;
import org.quackery.Test;

public class Helpers {
  public static Test successfulCase(String name) {
    return newCase(name, () -> {});
  }

  public static Test failingCase(String name, Throwable throwable) {
    return newCase(name, () -> {
      throw throwable;
    });
  }

  public static Optional<Throwable> thrownBy(Script script) {
    try {
      script.run();
      return Optional.empty();
    } catch (Throwable throwable) {
      return Optional.of(throwable);
    }
  }
}
