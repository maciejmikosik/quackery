package org.quackery.junit;

import static org.quackery.Suite.suite;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.run.Visitor;

public class FixBugs {
  public static Test fixBugs(Test test) {
    return fixNewlineBug(fixEmptySuiteBug(test));
  }

  public static List<Test> fixBugs(List<Test> tests) {
    List<Test> fixed = new ArrayList<>();
    for (Test test : tests) {
      fixed.add(fixBugs(test));
    }
    return fixed;
  }

  private static Test fixNewlineBug(Test test) {
    return new Visitor() {
      protected Test visit(Suite visiting) {
        Suite suite = (Suite) super.visit(visiting);
        return suite(fixNewlineBug(suite.name))
            .addAll(suite.tests);
      }

      protected Test visit(final Case visiting) {
        return new Case(fixNewlineBug(visiting.name)) {
          public void run() throws Throwable {
            visiting.run();
          }
        };
      }
    }.visit(test);
  }

  private static String fixNewlineBug(String name) {
    return name
        .replace('\n', ' ')
        .replace('\r', ' ');
  }

  private static Test fixEmptySuiteBug(Test test) {
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
