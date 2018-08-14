package org.quackery.junit;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static org.junit.runner.Description.createSuiteDescription;
import static org.junit.runner.Description.createTestDescription;
import static org.quackery.Suite.suite;
import static org.quackery.junit.FixBugs.fixBugs;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.quackery.Case;
import org.quackery.Quackery;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class QuackeryRunner extends Runner {
  private final Class<?> annotatedClass;
  private Description description;
  private Runner junitRunner;
  private List<Test> quackeryTests;

  /** This constructor is required by Runner contract and is invoked by junit. */
  public QuackeryRunner(Class<?> annotatedClass) {
    this.annotatedClass = annotatedClass;
    quackeryTests = instantiateQuackeryTestsDeclaredIn(annotatedClass);
    try {
      junitRunner = new BlockJUnit4ClassRunner(annotatedClass);
      description = junitRunner.getDescription();
      quackeryTests = fixBugs(quackeryTests);
      for (Test quackeryTest : quackeryTests) {
        description.addChild(describe(quackeryTest));
      }
    } catch (InitializationError error) {
      quackeryTests.addAll(instantiateFailingTestsExplainingCausesOf(error));
      Test root = fixBugs(quackeryTests.size() == 1
          ? quackeryTests.get(0)
          : suite(annotatedClass.getName())
              .addAll(quackeryTests));
      quackeryTests = asList(root);
      description = describe(root);
    }
  }

  public Description getDescription() {
    return description;
  }

  public void run(RunNotifier notifier) {
    for (Test test : quackeryTests) {
      run(test, notifier);
    }
    if (junitRunner != null) {
      junitRunner.run(notifier);
    }
  }

  private List<Test> instantiateQuackeryTestsDeclaredIn(Class<?> testClass) {
    List<Test> tests = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Quackery.class)) {
        tests.add(instantiateQuackeryTestReturnedBy(method));
      }
    }
    return tests;
  }

  private static Test instantiateQuackeryTestReturnedBy(Method method) {
    if (!isPublic(method.getModifiers())) {
      return failingCase(method, "method must be public");
    } else if (!isStatic(method.getModifiers())) {
      return failingCase(method, "method must be static");
    } else if (!Test.class.isAssignableFrom(method.getReturnType())) {
      return failingCase(method, "method return type must be assignable to " + Test.class.getName());
    } else if (method.getParameterTypes().length > 0) {
      return failingCase(method, "method cannot have parameters");
    }
    try {
      return (Test) method.invoke(null);
    } catch (final InvocationTargetException e) {
      return new Case(method.getName()) {
        public void run() throws Throwable {
          throw e.getCause();
        }
      };
    } catch (ReflectiveOperationException e) {
      throw new QuackeryException(e);
    }
  }

  private static Test failingCase(Method method, final String message) {
    return new Case(method.getName()) {
      public void run() {
        throw new QuackeryException(message);
      }
    };
  }

  private static List<Test> instantiateFailingTestsExplainingCausesOf(InitializationError error) {
    boolean hasJunitTestMethods = hasJunitTestMethods(error);

    List<Test> testsExplainingErrors = new ArrayList<>();
    for (final Throwable cause : error.getCauses()) {
      if (noRunnableMethods(cause)) {
        continue;
      } else if (noPublicDefaultConstructor(cause) && !hasJunitTestMethods) {
        continue;
      } else {
        testsExplainingErrors.add(new Case(cause.getMessage()) {
          public void run() throws Throwable {
            throw cause;
          }
        });
      }
    }
    return testsExplainingErrors;
  }

  private static boolean hasJunitTestMethods(InitializationError error) {
    for (Throwable cause : error.getCauses()) {
      if (noRunnableMethods(cause)) {
        return false;
      }
    }
    return true;
  }

  private static boolean noRunnableMethods(Throwable cause) {
    return cause.getMessage().equals("No runnable methods");
  }

  private static boolean noPublicDefaultConstructor(Throwable cause) {
    String message = cause.getMessage();
    return message.equals("Test class should have exactly one public constructor")
        || message.equals("Test class should have exactly one public zero-argument constructor");
  }

  private Description describe(Test test) {
    if (test instanceof Suite) {
      return describe((Suite) test);
    } else if (test instanceof Case) {
      return describe((Case) test);
    } else {
      throw new QuackeryException();
    }
  }

  private Description describe(Suite suite) {
    Description parent = createSuiteDescription(suite.name, id(suite));
    for (Test child : suite.tests) {
      parent.addChild(describe(child));
    }
    return parent;
  }

  private Description describe(Case cas) {
    return createTestDescription(annotatedClass.getName(), cas.name, id(cas));
  }

  private void run(Test test, RunNotifier notifier) {
    if (test instanceof Suite) {
      run((Suite) test, notifier);
    } else if (test instanceof Case) {
      run((Case) test, notifier);
    } else {
      throw new QuackeryException();
    }
  }

  private void run(Suite suite, RunNotifier notifier) {
    for (Test child : suite.tests) {
      run(child, notifier);
    }
  }

  private void run(Case cas, RunNotifier notifier) {
    Description described = describe(cas);
    notifier.fireTestStarted(described);
    try {
      cas.run();
    } catch (AssertException e) {
      Throwable wrapper = new AssertionError(e.getMessage(), e);
      notifier.fireTestFailure(new Failure(described, wrapper));
    } catch (AssumeException e) {
      Throwable wrapper = new AssumptionViolatedException(e.getMessage(), e);
      notifier.fireTestAssumptionFailed(new Failure(described, wrapper));
    } catch (Throwable throwable) {
      notifier.fireTestFailure(new Failure(described, throwable));
    } finally {
      notifier.fireTestFinished(described);
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
