package org.testanza;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.testanza.testers.describe_TestersForClasses_hasModifier;
import org.testanza.testers.describe_TestersForClasses_hasNoModifier;

public class describe_testanza {
  private static List<Throwable> failures = new ArrayList<Throwable>();

  public static void main(String[] args) throws Throwable {
    runTestsIn(describe_TestersForClasses_hasModifier.class);
    runTestsIn(describe_TestersForClasses_hasNoModifier.class);
    runTestsIn(describe_TestBuilder.class);
    runTestsIn(describe_Testers_asTester_Matcher.class);
    runTestsIn(describe_name_collisions.class);
    System.out.println(failures.size() + " failures");
    for (Throwable failure : failures) {
      System.out.println();
      failure.printStackTrace(System.out);
    }
  }

  private static void runTestsIn(Class<?> type) throws Throwable {
    for (Method method : type.getDeclaredMethods()) {
      if (isPublic(method) && isStatic(method) && hasNoParameters(method)) {
        try {
          method.invoke(null);
        } catch (InvocationTargetException e) {
          failures.add(e.getTargetException());
        }
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
