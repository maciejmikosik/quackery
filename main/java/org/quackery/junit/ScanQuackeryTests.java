package org.quackery.junit;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Quackery;
import org.quackery.QuackeryException;
import org.quackery.Test;

public class ScanQuackeryTests {
  public static List<Test> scanQuackeryTests(Class<?> testClass) {
    List<Test> tests = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Quackery.class)) {
        tests.add(instantiate(method));
      }
    }
    return tests;
  }

  private static Test instantiate(Method method) {
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
}
