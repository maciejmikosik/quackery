package org.quackery.run;

import static org.quackery.Suite.suite;
import static org.quackery.testing.Assertions.assertEquals;

import org.quackery.Case;
import org.quackery.Test;

public abstract class test_Runner extends test_Visitor {
  private final String name = "name";
  private Test test, report;
  private int invoked;

  public void runs_case_once() {
    test = new Case(name) {
      public void run() {
        invoked++;
      }
    };

    // when
    visit(test);

    // then
    assertEquals(invoked, 1);
  }

  public void runs_deep_case_once() {
    test = suite(name)
        .add(new Case(name) {
          public void run() {
            invoked++;
          }
        });

    // when
    visit(test);

    // then
    assertEquals(invoked, 1);
  }

  public void running_report_does_not_run_successful_case() throws Throwable {
    test = new Case(name) {
      public void run() {
        invoked++;
      }
    };
    report = visit(test);
    invoked = 0;

    // when
    ((Case) report).run();

    // then
    assertEquals(invoked, 0);
  }

  public void running_report_does_not_run_failed_case() throws Throwable {
    test = new Case(name) {
      public void run() {
        invoked++;
        throw new RuntimeException();
      }
    };
    report = visit(test);
    invoked = 0;

    // when
    try {
      ((Case) report).run();
    } catch (Throwable t) {}

    // then
    assertEquals(invoked, 0);
  }
}
