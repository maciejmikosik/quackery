package org.quackery;

import static java.math.RoundingMode.HALF_UP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.bytebuddy.test_ByteBuddy;

import org.quackery.contract.collection.test_detecting_bugs;
import org.quackery.contract.collection.test_illegal_use;
import org.quackery.contract.collection.test_passing_example_collections;
import org.quackery.contract.collection.test_passing_guava_collections;
import org.quackery.contract.collection.test_passing_jdk_collections;
import org.quackery.contract.collection.test_suite_naming;
import org.quackery.junit.test_QuackeryRunner_quackery_annotation;
import org.quackery.report.test_Reports_count_throwables;
import org.quackery.report.test_Reports_format;
import org.quackery.run.test_Runners_classLoaderScoped;
import org.quackery.run.test_Runners_run;
import org.quackery.run.test_Runners_runIn;
import org.quackery.run.test_Runners_threadScoped;

public class test_all {
  private static List<Throwable> failures = new ArrayList<Throwable>();
  private static List<String> statistics = new ArrayList<String>();

  public static void main(String[] args) throws Throwable {
    long start = System.nanoTime();

    runTestsIn(test_ByteBuddy.class);

    runTestsIn(test_Case.class);
    runTestsIn(test_Suite.class);
    runTestsIn(test_Runners_run.class);
    runTestsIn(test_Runners_runIn.class);
    runTestsIn(test_Runners_threadScoped.class);
    runTestsIn(test_Runners_classLoaderScoped.class);
    runTestsIn(test_Reports_count_throwables.class);
    runTestsIn(test_Reports_format.class);

    runTestsIn(test_QuackeryRunner_quackery_annotation.class);

    runTestsIn(test_illegal_use.class);
    runTestsIn(test_suite_naming.class);
    runTestsIn(test_detecting_bugs.class);
    runTestsIn(test_passing_example_collections.class);
    runTestsIn(test_passing_jdk_collections.class);
    runTestsIn(test_passing_guava_collections.class);

    long stop = System.nanoTime();
    BigDecimal time = new BigDecimal(BigInteger.valueOf(stop - start), 9).setScale(3, HALF_UP);
    System.out.println("finished in " + time + " seconds");

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
    for (Method method : type.getMethods()) {
      if (!isJdkMethod(method) && isPublic(method) && !isStatic(method) && hasNoParameters(method)) {
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

  private static boolean isJdkMethod(Method method) {
    return method.getDeclaringClass().getClassLoader() == null;
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
