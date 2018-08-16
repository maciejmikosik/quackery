package org.quackery.run;

import static org.quackery.run.TestingVisitors.visitor_runs_cases;

import org.quackery.Test;

public abstract class TestRunner extends TestVisitor {
  public void basic_runner_test() throws Throwable {
    Visitor visitor = new Visitor() {
      public Test visit(Test visiting) {
        return TestRunner.this.visit(visiting);
      }
    };
    visitor_runs_cases(visitor);
  }
}
