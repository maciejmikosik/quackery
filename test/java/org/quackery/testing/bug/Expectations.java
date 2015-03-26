package org.quackery.testing.bug;

import org.quackery.Case;
import org.quackery.Contract;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.testing.Problem;
import org.quackery.testing.Report;

public class Expectations {
  public static void expectSuccess(Contract<Class<?>> contract, Class<?> implementation) {
    Test test = contract.test(implementation);
    Report report = runAndCatch(test);
    boolean expected = report.problems.size() == 0;
    if (!expected) {
      throw new AssertionError("expected success of " + implementation.getName() + report);
    }
  }

  public static void expectFailure(Contract<Class<?>> contract, Class<?> implementation) {
    Test test = contract.test(implementation);
    Report report = runAndCatch(test);
    boolean expected = report.failures().size() > 0 && report.errors().size() == 0;
    if (!expected) {
      throw new AssertionError("expected failure of " + implementation.getName() + report);
    }
  }

  private static Report runAndCatch(Test test) {
    if (test instanceof Case) {
      Case cas = (Case) test;
      try {
        cas.run();
        return new Report();
      } catch (Throwable t) {
        return new Report().add(new Problem(cas, t));
      }
    } else if (test instanceof Suite) {
      Report report = new Report();
      for (Test subtest : ((Suite) test).tests) {
        report = report.merge(runAndCatch(subtest));
      }
      return report;
    } else {
      throw new RuntimeException("");
    }
  }
}
