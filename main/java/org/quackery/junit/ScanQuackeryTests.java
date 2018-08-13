package org.quackery.junit;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.quackery.QuackeryException.check;

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
    check(isPublic(method.getModifiers()));
    check(isStatic(method.getModifiers()));
    check(Test.class.isAssignableFrom(method.getReturnType()));
    check(method.getParameterTypes().length == 0);
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
}
