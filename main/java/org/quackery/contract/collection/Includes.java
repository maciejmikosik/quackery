package org.quackery.contract.collection;

import static org.quackery.Tests.deep;
import static org.quackery.Tests.ifSuite;
import static org.quackery.Tests.remove;
import static org.quackery.help.Helpers.failingStory;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class Includes {
  private static final Test EXCLUDED = failingStory(
      "EXCLUDED",
      new QuackeryException("should be excluded"));

  public static Test includeIf(boolean condition, Test test) {
    return condition
        ? test
        : EXCLUDED;
  }

  public static Test filterIncluded(Test test) {
    return deep(ifSuite(remove(t -> t == EXCLUDED)))
        .apply(test);
  }
}
