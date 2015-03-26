package org.quackery.testing.bug;

import static org.quackery.testing.Tests.runQuietly;

import org.quackery.Contract;
import org.quackery.Test;
import org.quackery.testing.Report;

public class Expectations {
  public static void expectSuccess(Contract<Class<?>> contract, Class<?> implementation) {
    Test test = contract.test(implementation);
    Report report = runQuietly(test);
    boolean expected = report.problems.size() == 0;
    if (!expected) {
      throw new AssertionError("expected success of " + implementation.getName() + report);
    }
  }

  public static void expectFailure(Contract<Class<?>> contract, Class<?> implementation) {
    Test test = contract.test(implementation);
    Report report = runQuietly(test);
    boolean expected = report.failures().size() > 0 && report.errors().size() == 0;
    if (!expected) {
      throw new AssertionError("expected failure of " + implementation.getName() + report);
    }
  }
}
