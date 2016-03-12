package org.quackery.junit;

import static org.quackery.Suite.suite;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.run.Visitor;

public class FixNewlineBug {
  public static Test fixNewlineBug(Test test) {
    return new Visitor() {
      protected Test visit(Suite visiting) {
        Suite suite = (Suite) super.visit(visiting);
        return suite(fix(suite.name)).addAll(suite.tests);
      }

      protected Test visit(final Case visiting) {
        return new Case(fix(visiting.name)) {
          public void run() throws Throwable {
            visiting.run();
          }
        };
      }
    }.visit(test);
  }

  private static String fix(String name) {
    return name
        .replace('\n', ' ')
        .replace('\r', ' ');
  }
}
