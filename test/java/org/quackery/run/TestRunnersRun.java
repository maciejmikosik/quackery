package org.quackery.run;

import static org.quackery.run.Runners.run;
import static org.quackery.run.TestingVisitors.visitor_preserves_case_result;
import static org.quackery.run.TestingVisitors.visitor_preserves_names_and_structure;
import static org.quackery.run.TestingVisitors.visitor_runs_cases_eagerly;
import static org.quackery.run.TestingVisitors.visitor_validates_arguments;

import org.quackery.Test;

public class TestRunnersRun {
  public static void test_runners_run() throws Throwable {
    Visitor visitor = new Visitor() {
      public Test visit(Test visiting) {
        return run(visiting);
      }
    };

    visitor_preserves_names_and_structure(visitor);
    visitor_preserves_case_result(visitor);
    visitor_validates_arguments(visitor);
    visitor_runs_cases_eagerly(visitor);
  }
}
