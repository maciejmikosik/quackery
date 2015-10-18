package org.quackery.junit;

import static org.junit.runner.Description.createSuiteDescription;
import static org.junit.runner.Description.createTestDescription;
import static org.quackery.junit.FixEmptySuiteBug.fixEmptySuiteBug;
import static org.quackery.junit.Instantiate.instantiate;

import java.io.Serializable;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.quackery.AssertionException;
import org.quackery.AssumptionException;
import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;

public class QuackeryRunner extends Runner {
  private final Test test;

  public QuackeryRunner(Class<?> testClass) {
    test = fixEmptySuiteBug(instantiate(testClass));
  }

  public Description getDescription() {
    return describe(test);
  }

  private static Description describe(Test test) {
    if (test instanceof Suite) {
      return describe((Suite) test);
    } else if (test instanceof Case) {
      return describe((Case) test);
    } else {
      throw new QuackeryException();
    }
  }

  private static Description describe(Suite suite) {
    Description parent = createSuiteDescription(suite.name, id(suite));
    for (Test child : suite.tests) {
      parent.addChild(describe(child));
    }
    return parent;
  }

  private static Description describe(Case cas) {
    return createTestDescription(cas.getClass().getName(), cas.name, id(cas));
  }

  public void run(RunNotifier notifier) {
    run(test, notifier);
  }

  private static void run(Test test, RunNotifier notifier) {
    if (test instanceof Suite) {
      run((Suite) test, notifier);
    } else if (test instanceof Case) {
      run((Case) test, notifier);
    } else {
      throw new QuackeryException();
    }
  }

  private static void run(Suite suite, RunNotifier notifier) {
    for (Test child : suite.tests) {
      run(child, notifier);
    }
  }

  private static void run(Case cas, RunNotifier notifier) {
    Description description = describe(cas);
    notifier.fireTestStarted(description);
    try {
      cas.run();
    } catch (AssertionException e) {
      Throwable wrapper = new AssertionError(e.getMessage(), e);
      notifier.fireTestFailure(new Failure(description, wrapper));
    } catch (AssumptionException e) {
      Throwable wrapper = new AssumptionViolatedException(e.getMessage(), e);
      notifier.fireTestAssumptionFailed(new Failure(description, wrapper));
    } catch (Throwable throwable) {
      notifier.fireTestFailure(new Failure(description, throwable));
    } finally {
      notifier.fireTestFinished(description);
    }
  }

  private static Serializable id(Test test) {
    final int id = System.identityHashCode(test);
    return new Serializable() {
      public boolean equals(Object obj) {
        return getClass() == obj.getClass() && hashCode() == obj.hashCode();
      }

      public int hashCode() {
        return id;
      }

      public String toString() {
        return Integer.toString(id);
      }
    };
  }
}
