package org.quackery.run;

import static org.quackery.QuackeryException.check;
import static org.quackery.Suite.suite;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Runner {
  public Test run(Test test) {
    check(test != null);
    return test instanceof Case
        ? run((Case) test)
        : run((Suite) test);
  }

  private Case run(Case test) {
    try {
      test.run();
    } catch (final Throwable throwable) {
      return new Case(test.name) {
        public void run() throws Throwable {
          throw throwable;
        }
      };
    }
    return new Case(test.name) {
      public void run() {}
    };
  }

  private Suite run(Suite test) {
    List<Test> reports = new ArrayList<>();
    for (Test child : test.tests) {
      reports.add(child instanceof Case
          ? run((Case) child)
          : run((Suite) child));
    }
    return suite(test.name)
        .testAll(reports);
  }
}
