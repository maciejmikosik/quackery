package org.quackery.run;

import static org.quackery.Suite.suite;
import static org.quackery.run.Runners.run;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockCase;

import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;

public class test_Runners_run {
  private final String name = "name", nameA = "nameA", nameB = "nameB", nameC = "nameC",
      nameD = "nameD", nameE = "nameE", nameF = "nameF";
  private Test test, report;
  private Throwable throwable = new Throwable();
  private int invoked;

  public void report_keeps_successful_case_name() {
    test = mockCase(name);

    // when
    report = run(test);

    // then
    assertEquals(report.name, name);
  }

  public void report_keeps_failed_case_name() {
    test = mockCase(name, throwable);

    // when
    report = run(test);

    // then
    assertEquals(report.name, name);
  }

  public void report_keeps_deep_case_name() {
    test = suite(nameA)
        .test(mockCase(name));

    // when
    report = run(test);

    // then
    assertEquals(navigate(report, 0).name, name);
  }

  public void report_imitates_successful_case() throws Throwable {
    test = mockCase(name);
    report = run(test);

    // when
    ((Case) report).run();

    // then no exception
  }

  public void report_imitates_failed_case() throws Throwable {
    class SomeThrowable extends Throwable {}
    throwable = new SomeThrowable();
    test = mockCase(name, throwable);
    report = run(test);

    try {
      // when
      ((Case) report).run();
      fail();
      // then
    } catch (SomeThrowable t) {
      assertEquals(t, throwable);
    }
  }

  public void runner_runs_case_once() {
    test = new Case(name) {
      public void run() {
        invoked++;
      }
    };

    // when
    run(test);

    // then
    assertEquals(invoked, 1);
  }

  public void runner_runs_deep_case_once() {
    test = suite(name)
        .test(new Case(name) {
          public void run() {
            invoked++;
          }
        });

    // when
    run(test);

    // then
    assertEquals(invoked, 1);
  }

  public void running_report_does_not_run_successful_case() throws Throwable {
    test = new Case(name) {
      public void run() {
        invoked++;
      }
    };
    report = run(test);
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
    report = run(test);
    invoked = 0;

    // when
    try {
      ((Case) report).run();
    } catch (Throwable t) {}

    // then
    assertEquals(invoked, 0);
  }

  public void report_keeps_suite_name() {
    test = suite(name);

    // when
    report = run(test);

    // then
    assertEquals(report.name, name);
  }

  public void report_copies_test_hierarchy() {
    test = suite(name)
        .test(suite(nameA)
            .test(mockCase(nameC))
            .test(mockCase(nameD)))
        .test(suite(nameB)
            .test(mockCase(nameE))
            .test(mockCase(nameF)));

    // when
    report = run(test);

    // then
    assertEquals(report.name, name);
    assertEquals(navigate(Suite.class, report).tests.size(), 2);
    assertEquals(navigate(report, 0).name, nameA);
    assertEquals(navigate(report, 1).name, nameB);

    assertEquals(navigate(Suite.class, report, 0).tests.size(), 2);
    assertEquals(navigate(report, 0, 0).name, nameC);
    assertEquals(navigate(report, 0, 1).name, nameD);

    assertEquals(navigate(Suite.class, report, 1).tests.size(), 2);
    assertEquals(navigate(report, 1, 0).name, nameE);
    assertEquals(navigate(report, 1, 1).name, nameF);
  }

  public void cannot_run_null() {
    try {
      run(null);
      fail();
    } catch (QuackeryException e) {}
  }

  private static Test navigate(Test test, int... path) {
    Test result = test;
    for (int index : path) {
      result = ((Suite) result).tests.get(index);
    }
    return result;
  }

  private static <T extends Test> T navigate(Class<T> cast, Test test, int... path) {
    Test result = test;
    for (int index : path) {
      result = ((Suite) result).tests.get(index);
    }
    return (T) result;
  }
}
