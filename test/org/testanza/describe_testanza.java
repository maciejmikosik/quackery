package org.testanza;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.testanza.testers.describe_TestMembers_has_modifier;

public class describe_testanza {
  public static void main(String[] args) throws Throwable {
    runTestsIn(describe_TestMembers_has_modifier.class);
    runTestsIn(describe_BodyTest.class);
    runTestsIn(describe_TestBuilder.class);
    System.out.println("successful");
  }

  public static void verify(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }

  private static void runTestsIn(Class<?> type) throws Throwable {
    for (Method method : type.getDeclaredMethods()) {
      if (isPublic(method) && isStatic(method) && hasNoParameters(method)) {
        method.invoke(null);
      }
    }
  }

  private static boolean isPublic(Method method) {
    return Modifier.isPublic(method.getModifiers());
  }

  private static boolean isStatic(Method method) {
    return Modifier.isStatic(method.getModifiers());
  }

  private static boolean hasNoParameters(Method method) {
    return method.getParameterTypes().length == 0;
  }
}
