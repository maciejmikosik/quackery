package org.quackery.testing;

import org.quackery.AssertionException;
import org.quackery.AssumptionException;
import org.quackery.Case;

public class Problem {
  public final Case test;
  public final Throwable throwable;

  public Problem(Case test, Throwable problem) {
    this.test = test;
    this.throwable = problem;
  }

  public boolean isSuccess() {
    return throwable == null;
  }

  public boolean isFailure() {
    return throwable instanceof AssertionException;
  }

  public boolean isMisassumption() {
    return throwable instanceof AssumptionException;
  }

  public boolean isError() {
    return !isSuccess() && !isFailure() && !isMisassumption();
  }
}
