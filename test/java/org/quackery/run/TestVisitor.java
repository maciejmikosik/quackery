package org.quackery.run;

import static org.quackery.run.TestingVisitors.visitor_preserves_case_result;
import static org.quackery.run.TestingVisitors.visitor_preserves_names_and_structure;
import static org.quackery.run.TestingVisitors.visitor_validates_arguments;

import org.quackery.Test;

public abstract class TestVisitor {
  protected abstract Test visit(Test visiting);

  public void basic_test_visitor() throws Throwable {
    Visitor visitor = new Visitor() {
      public Test visit(Test visiting) {
        return TestVisitor.this.visit(visiting);
      }
    };
    visitor_preserves_names_and_structure(visitor);
    visitor_preserves_case_result(visitor);
    visitor_validates_arguments(visitor);
  }
}
