package org.quackery.help;

import static org.quackery.Story.story;

import java.util.Optional;

import org.quackery.Script;
import org.quackery.Test;

public class Helpers {
  public static Test successfulStory(String name) {
    return story(name, () -> {});
  }

  public static Test failingStory(String name, Throwable throwable) {
    return story(name, () -> {
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
