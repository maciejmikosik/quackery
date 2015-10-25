package org.quackery.run;

import org.quackery.Case;
import org.quackery.Test;

public class Runners {
  public static Test run(Test test) {
    return new Visitor() {
      protected Case visit(Case visiting) {
        return run(visiting);
      }
    }.visit(test);
  }

  private static Case run(Case visiting) {
    try {
      visiting.run();
    } catch (final Throwable throwable) {
      return new Case(visiting.name) {
        public void run() throws Throwable {
          throw throwable;
        }
      };
    }
    return new Case(visiting.name) {
      public void run() {}
    };
  }
}
