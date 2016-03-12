package org.quackery.junit;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.run.Visitor;

public class FixEmptySuiteBug {
  public static Test fixEmptySuiteBug(Test test) {
    return new Visitor() {
      protected Test visit(Suite visiting) {
        Suite suite = (Suite) super.visit(visiting);
        return suite.tests.isEmpty()
            ? successfulCase(suite.name)
            : suite;
      }
    }.visit(test);
  }

  private static Case successfulCase(String name) {
    return new Case(name) {
      public void run() {}
    };
  }
}
