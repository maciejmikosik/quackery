package org.quackery.junit;

import static org.quackery.help.Helpers.rename;
import static org.quackery.help.Helpers.successfulCase;

import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.help.TraversingDecorator;

public class FixBugs {
  public static Test fixBugs(Test root) {
    return fixNewlineBug(fixEmptySuiteBug(fixEmptyNameBug(root)));
  }

  private static Test fixNewlineBug(Test root) {
    return new TraversingDecorator() {
      protected Test decorateTest(Test test) {
        String newName = test.name
            .replace('\n', ' ')
            .replace('\r', ' ');
        return rename(newName, test);
      }
    }.decorate(root);
  }

  private static Test fixEmptySuiteBug(Test root) {
    return new TraversingDecorator() {
      protected Test decorateSuite(Suite suite) {
        return suite.tests.isEmpty()
            ? successfulCase(suite.name)
            : suite;
      }
    }.decorate(root);
  }

  private static Test fixEmptyNameBug(Test root) {
    return new TraversingDecorator() {
      protected Test decorateTest(Test test) {
        return test.name.isEmpty()
            ? rename("[empty_name]", test)
            : test;
      }
    }.decorate(root);
  }
}
