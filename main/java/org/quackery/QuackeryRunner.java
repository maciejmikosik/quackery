package org.quackery;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.runner.Description.createSuiteDescription;
import static org.junit.runner.Description.createTestDescription;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class QuackeryRunner extends Runner {
  private final Test test;

  public QuackeryRunner(Class<?> testClass) throws InitializationError {
    test = instantiateTest(testClass);
  }

  private static Test instantiateTest(Class<?> testClass) throws InitializationError {
    List<Method> methods = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Quackery.class)) {
        checkInitialization(isPublic(method.getModifiers()));
        checkInitialization(isStatic(method.getModifiers()));
        checkInitialization(Test.class.isAssignableFrom(method.getReturnType()));
        checkInitialization(method.getParameterTypes().length == 0);
        methods.add(method);
      }
    }
    checkInitialization(methods.size() == 1);
    try {
      return (Test) methods.get(0).invoke(null);
    } catch (ReflectiveOperationException e) {
      throw new InitializationError(e);
    }
  }

  private static void checkInitialization(boolean condition) throws InitializationError {
    if (!condition) {
      throw new InitializationError("");
    }
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
    } catch (FailureException e) {
      Throwable wrapper = new AssertionError(e.getMessage(), e);
      notifier.fireTestFailure(new Failure(description, wrapper));
    } catch (QuackeryAssumptionException e) {
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
