package org.testanza;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class AnnotatedTestCase extends TestCase {
  public AnnotatedTestCase(String name) {
    super(name);
  }

  protected void runTest() throws Throwable {
    Method testMethod = null;
    try {
      List<Method> testMethods = testMethods(getClass());
      if (testMethods.size() != 1) {
        throw new RuntimeException();
      }
      testMethod = testMethods.get(0);
      testMethod.setAccessible(true);
      testMethod.invoke(this);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (ReflectiveOperationException e) {
      throw e;
    }
  }

  private static List<Method> testMethods(Class<?> type) {
    List<Method> testMethods = new ArrayList<Method>();
    for (Method method : type.getDeclaredMethods()) {
      if (method.isAnnotationPresent(org.junit.Test.class)) {
        testMethods.add(method);
      }
    }
    return testMethods;
  }
}
