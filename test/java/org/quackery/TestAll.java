package org.quackery;

import static java.math.RoundingMode.HALF_UP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.quackery.contract.collection.TestDetectingBugs;
import org.quackery.contract.collection.TestIllegalUse;
import org.quackery.contract.collection.TestPassingExampleCollections;
import org.quackery.contract.collection.TestPassingGuavaCollections;
import org.quackery.contract.collection.TestPassingJdkCollections;
import org.quackery.contract.collection.TestSuiteNaming;
import org.quackery.junit.TestQuackeryRunnerDescriptionHierarchy;
import org.quackery.junit.TestQuackeryRunnerIgnoreAnnotation;
import org.quackery.junit.TestQuackeryRunnerJunitTestAnnotation;
import org.quackery.junit.TestQuackeryRunnerQuackeryAnnotation;
import org.quackery.report.TestReportsCountThrowables;
import org.quackery.report.TestReportsFormat;
import org.quackery.run.TestRunnersClassLoaderScoped;
import org.quackery.run.TestRunnersRun;
import org.quackery.run.TestRunnersRunIn;
import org.quackery.run.TestRunnersThreadScoped;

import net.bytebuddy.TestByteBuddy;

public class TestAll {
  private static List<Throwable> failures = new ArrayList<Throwable>();
  private static List<String> statistics = new ArrayList<String>();

  public static void main(String[] args) throws Throwable {
    long start = System.nanoTime();

    runTestsIn(TestByteBuddy.class);

    runTestsIn(TestCase.class);
    runTestsIn(TestSuite.class);
    runTestsIn(TestRunnersRun.class);
    runTestsIn(TestRunnersRunIn.class);
    runTestsIn(TestRunnersThreadScoped.class);
    runTestsIn(TestRunnersClassLoaderScoped.class);
    runTestsIn(TestReportsCountThrowables.class);
    runTestsIn(TestReportsFormat.class);

    runTestsIn(TestQuackeryRunnerQuackeryAnnotation.class);
    runTestsIn(TestQuackeryRunnerJunitTestAnnotation.class);
    runTestsIn(TestQuackeryRunnerDescriptionHierarchy.class);
    runTestsIn(TestQuackeryRunnerIgnoreAnnotation.class);

    runTestsIn(TestIllegalUse.class);
    runTestsIn(TestSuiteNaming.class);
    runTestsIn(TestDetectingBugs.class);
    runTestsIn(TestPassingExampleCollections.class);
    runTestsIn(TestPassingJdkCollections.class);
    runTestsIn(TestPassingGuavaCollections.class);

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
