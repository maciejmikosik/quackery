package org.quackery.junit;

import static org.quackery.Suite.suite;
import static org.quackery.help.Helpers.successfulCase;
import static org.quackery.help.Helpers.traverseNames;
import static org.quackery.help.Helpers.traverseSuites;

import org.quackery.Test;

public class FixBugs {
  public static Test fixBugs(Test root) {
    return fixNewlineBug(fixEmptySuiteBug(fixEmptyNameBug(root)));
  }

  private static Test fixNewlineBug(Test root) {
    return traverseNames(root,
        name -> name
            .replace('\n', ' ')
            .replace('\r', ' '));
  }

  private static Test fixEmptySuiteBug(Test root) {
    return traverseSuites(root,
        (name, children) -> children.isEmpty()
            ? successfulCase(name)
            : suite(name).addAll(children));
  }

  private static Test fixEmptyNameBug(Test root) {
    return traverseNames(root,
        name -> name.isEmpty()
            ? "[empty_name]"
            : name);
  }
}
