package org.quackery.junit;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.runner.Description.createSuiteDescription;
import static org.junit.runner.Description.createTestDescription;
import static org.quackery.QuackeryException.check;
import static org.quackery.Suite.suite;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.quackery.AssertionException;
import org.quackery.AssumptionException;
import org.quackery.Case;
import org.quackery.Quackery;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;

public class QuackeryRunner extends Runner {
  private final Test test;

  public QuackeryRunner(Class<?> testClass) {
    test = fix(instantiateTest(testClass));
  }

  private static Test instantiateTest(Class<?> testClass) {
    List<Method> methods = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Quackery.class)) {
        check(isPublic(method.getModifiers()));
        check(isStatic(method.getModifiers()));
        check(Test.class.isAssignableFrom(method.getReturnType()));
        check(method.getParameterTypes().length == 0);
        methods.add(method);
      }
    }
    check(methods.size() > 0);
    if (methods.size() == 1) {
      return testReturnedFrom(methods.get(0));
    } else {
      Suite suite = suite(testClass.getName());
      for (Method method : methods) {
        suite = suite.test(testReturnedFrom(method));
      }
      return suite;
    }
  }

  private static Test testReturnedFrom(Method method) {
    try {
      return (Test) method.invoke(null);
    } catch (ReflectiveOperationException e) {
      throw new QuackeryException(e);
    }
  }

  private static Test fix(Test test) {
    return test instanceof Suite
        ? fix((Suite) test)
        : test;
  }

  private static Test fix(Suite suite) {
    return suite.tests.isEmpty()
        ? fixEmptySuite(suite)
        : fixChildren(suite);
  }

  private static Case fixEmptySuite(Suite suite) {
    return new Case(suite.name) {
      public void run() {}
    };
  }

  private static Test fixChildren(Suite suite) {
    Suite fixedSuite = suite(suite.name);
    for (Test test : suite.tests) {
      fixedSuite = fixedSuite.test(fix(test));
    }
    return fixedSuite;
  }

  public Description getDescription() {
    return describeRecursively(test);
  }

  private static Description describeRecursively(Test test) {
    if (test instanceof Suite) {
      Suite suite = (Suite) test;
      Description parent = describe(suite);
      for (Test child : suite.tests) {
        parent.addChild(describeRecursively(child));
      }
      return parent;
    } else if (test instanceof Case) {
      return describe((Case) test);
    } else {
      throw new QuackeryException();
    }
  }

  private static Description describe(Suite suite) {
    return createSuiteDescription(suite.name, id(suite));
  }

  private static Description describe(Case test) {
    return createTestDescription(test.getClass().getName(), test.name, id(test));
  }

  public void run(RunNotifier notifier) {
    runRecursively(test, notifier);
  }

  private static void runRecursively(Test test, RunNotifier notifier) {
    if (test instanceof Suite) {
      for (Test child : ((Suite) test).tests) {
        runRecursively(child, notifier);
      }
    } else if (test instanceof Case) {
      run((Case) test, notifier);
    } else {
      throw new QuackeryException();
    }
  }

  private static void run(Case test, RunNotifier notifier) {
    Description description = describe(test);
    notifier.fireTestStarted(description);
    try {
      test.run();
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
