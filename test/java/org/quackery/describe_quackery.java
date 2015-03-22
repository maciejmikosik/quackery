package org.quackery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class describe_quackery {
  private static List<Throwable> failures = new ArrayList<Throwable>();
  private static List<String> statistics = new ArrayList<String>();

  public static void main(String[] args) throws Throwable {
    runTestsIn(describe_Case.class);
    runTestsIn(describe_Suite.class);
    runTestsIn(describe_QuackeryRunner.class);
    runTestsIn(describe_Testers_hasModifier.class);
    runTestsIn(describe_Testers_hasNoModifier.class);
    runTestsIn(describe_Testers_hasConstructor.class);
    runTestsIn(describe_Testers_isAssignableTo_Class.class);
    runTestsIn(describe_Quacks_quacksLike_collection.class);

    if (failures.size() == 0) {
      System.out.println("no failures");
      for (String stat : statistics) {
        System.out.println(stat);
      }
    } else {
      System.out.println(failures.size() + " failures");
      for (Throwable failure : failures) {
        System.out.println();
        failure.printStackTrace(System.out);
      }
    }
  }

  private static void runTestsIn(Class<?> type) throws Throwable {
    int count = 0;
    for (Method method : type.getDeclaredMethods()) {
      if (isPublic(method) && !isStatic(method) && hasNoParameters(method)) {
        count++;
        try {
          method.invoke(type.newInstance());
        } catch (InvocationTargetException e) {
          failures.add(e.getTargetException());
        }
      }
    }
    statistics.add(type.getSimpleName() + " : " + count);
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
