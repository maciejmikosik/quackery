package org.quackery.junit;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.quackery.QuackeryException.check;
import static org.quackery.Suite.suite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.quackery.Quackery;
import org.quackery.QuackeryException;
import org.quackery.Test;

public class Instantiate {
  public static Test instantiate(Class<?> testClass) {
    List<Test> tests = instantiate(annotatedMethods(testClass));
    check(tests.size() > 0);
    return tests.size() == 1
        ? tests.get(0)
        : suite(testClass.getName()).testAll(tests);
  }

  private static List<Method> annotatedMethods(Class<?> testClass) {
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
    return methods;
  }

  private static List<Test> instantiate(List<Method> methods) {
    List<Test> tests = new ArrayList<>();
    for (Method method : methods) {
      tests.add(instantiate(method));
    }
    return tests;
  }

  private static Test instantiate(Method method) {
    try {
      return (Test) method.invoke(null);
    } catch (ReflectiveOperationException e) {
      throw new QuackeryException(e);
    }
  }
}
