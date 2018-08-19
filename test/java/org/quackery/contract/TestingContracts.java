package org.quackery.contract;

import static org.quackery.report.Reports.count;
import static org.quackery.report.Reports.format;
import static org.quackery.run.Runners.run;

import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class TestingContracts {
  public static void assertSuccess(Test test) {
    Test report = run(test);
    int problems = count(Throwable.class, report);
    boolean expected = problems == 0;
    if (!expected) {
      throw new AssertionError("expected success but was\n\n" + format(report));
    }
  }

  public static void assertFailure(Test test) {
    Test report = run(test);

    int failures = count(AssertException.class, report);
    int misassumptions = count(AssumeException.class, report);
    int problems = count(Throwable.class, report);
    int errors = problems - failures - misassumptions;

    boolean expected = failures > 0 && errors == 0;
    if (!expected) {
      throw new AssertionError("expected failure but was\n\n" + format(report));
    }
  }
}
