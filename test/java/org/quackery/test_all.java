package org.quackery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.quackery.contract.collection.test_collection_contract;
import org.quackery.contract.collection.test_detecting_alien_types;
import org.quackery.contract.collection.test_example_collections;
import org.quackery.contract.collection.test_guava_collections;
import org.quackery.contract.collection.test_illegal_use;
import org.quackery.contract.collection.test_jdk_collections;
import org.quackery.contract.collection.test_list_contract;
import org.quackery.contract.collection.test_mutable_collection_contract;
import org.quackery.contract.collection.test_mutable_list_contract;
import org.quackery.run.test_Runner;
import org.quackery.run.test_Reports_count_throwables;

public class test_all {
  private static List<Throwable> failures = new ArrayList<Throwable>();
  private static List<String> statistics = new ArrayList<String>();

  public static void main(String[] args) throws Throwable {
    runTestsIn(test_Case.class);
    runTestsIn(test_Suite.class);
    runTestsIn(test_Runner.class);
    runTestsIn(test_Reports_count_throwables.class);

    runTestsIn(test_QuackeryRunner.class);

    runTestsIn(test_illegal_use.class);
    runTestsIn(test_collection_contract.class);
    runTestsIn(test_mutable_collection_contract.class);
    runTestsIn(test_list_contract.class);
    runTestsIn(test_mutable_list_contract.class);
    runTestsIn(test_example_collections.class);
    runTestsIn(test_detecting_alien_types.class);
    runTestsIn(test_jdk_collections.class);
    runTestsIn(test_guava_collections.class);

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
