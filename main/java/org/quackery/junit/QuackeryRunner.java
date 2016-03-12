package org.quackery.junit;

import static org.junit.runner.Description.createSuiteDescription;
import static org.junit.runner.Description.createTestDescription;
import static org.quackery.Suite.suite;
import static org.quackery.junit.FixEmptySuiteBug.fixEmptySuiteBug;
import static org.quackery.junit.FixNewlineBug.fixNewlineBug;
import static org.quackery.junit.ScanJunitTests.scanJunitTests;
import static org.quackery.junit.ScanQuackeryTests.scanQuackeryTests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class QuackeryRunner extends Runner {
  private final Runner delegate;

  public QuackeryRunner(Class<?> annotatedClass) {
    List<Test> quackeryTests = scanQuackeryTests(annotatedClass);
    Runner junitRunner = scanJunitTests(annotatedClass);
    delegate = junitRunner == null
        ? quackeryTestsRunner(annotatedClass, quackeryTests)
        : quackeryAndJunitTestsRunner(quackeryTests, junitRunner);
  }

  public Description getDescription() {
    return delegate.getDescription();
  }

  public void run(RunNotifier notifier) {
    delegate.run(notifier);
  }

  private static Runner quackeryTestsRunner(Class<?> annotatedClass, List<Test> tests) {
    final Test root = fixAllBugs(tests.size() == 1
        ? tests.get(0)
        : suite(annotatedClass.getName()).addAll(tests));
    final Description description = describe(root);
    return new Runner() {
      public Description getDescription() {
        return description;
      }

      public void run(RunNotifier notifier) {
        QuackeryRunner.run(root, notifier);
      }
    };
  }

  private static Runner quackeryAndJunitTestsRunner(final List<Test> tests, final Runner junitRunner) {
    final Description description = junitRunner.getDescription();
    final List<Test> fixedTests = new ArrayList<>();
    for (Test test : tests) {
      fixedTests.add(fixAllBugs(test));
    }
    for (Test test : fixedTests) {
      description.addChild(describe(test));
    }
    return new Runner() {
      public Description getDescription() {
        return description;
      }

      public void run(RunNotifier notifier) {
        for (Test test : fixedTests) {
          QuackeryRunner.run(test, notifier);
        }
        junitRunner.run(notifier);
      }
    };
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
    } catch (AssertException e) {
      Throwable wrapper = new AssertionError(e.getMessage(), e);
      notifier.fireTestFailure(new Failure(description, wrapper));
    } catch (AssumeException e) {
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

  private static Test fixAllBugs(Test test) {
    return fixNewlineBug(fixEmptySuiteBug(test));
  }
}
