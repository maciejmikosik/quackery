package org.quackery.junit;

import static org.quackery.Tests.deep;
import static org.quackery.Tests.ifSuite;
import static org.quackery.Tests.onName;
import static org.quackery.help.Helpers.successfulCase;

import org.quackery.Test;

public class FixBugs {
  public static Test fixBugs(Test root) {
    return fixNewlineBug(fixEmptySuiteBug(fixEmptyNameBug(root)));
  }

  private static Test fixNewlineBug(Test root) {
    return deep(onName(name -> name
        .replace('\n', ' ')
        .replace('\r', ' ')))
            .apply(root);
  }

  private static Test fixEmptySuiteBug(Test root) {
    return deep(ifSuite(suite -> suite.children.isEmpty()
        ? successfulCase(suite.name)
        : suite))
            .apply(root);
  }

  private static Test fixEmptyNameBug(Test root) {
    return deep(onName(name -> name.isEmpty()
        ? "[empty_name]"
        : name))
            .apply(root);
  }
}
