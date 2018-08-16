package org.quackery;

import static java.lang.String.format;
import static java.math.RoundingMode.HALF_UP;
import static net.bytebuddy.TestByteBuddy.test_byte_buddy;
import static org.quackery.TestCase.test_case;
import static org.quackery.TestSuite.test_suite;
import static org.quackery.contract.collection.TestCollectionContract.test_collection_contract;
import static org.quackery.report.TestReportsCountThrowables.test_reports_count_throwables;
import static org.quackery.report.TestReportsFormat.test_reports_format;
import static org.quackery.run.TestRunnersClassLoaderScoped.test_runners_class_loader_scoped;
import static org.quackery.run.TestRunnersRun.test_runners_run;
import static org.quackery.run.TestRunnersRunIn.test_runners_run_in;
import static org.quackery.run.TestRunnersThreadScoped.test_runners_thread_scoped;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.quackery.junit.TestQuackeryRunnerDescriptionHierarchy;
import org.quackery.junit.TestQuackeryRunnerIgnoreAnnotation;
import org.quackery.junit.TestQuackeryRunnerInitialization;
import org.quackery.junit.TestQuackeryRunnerJunitTestAnnotation;
import org.quackery.junit.TestQuackeryRunnerQuackeryAnnotation;

public class TestAll {
  private static List<Throwable> failures = new ArrayList<Throwable>();
  private static List<String> statistics = new ArrayList<String>();

  public static void main(String[] args) throws Throwable {
    long start = System.nanoTime();

    test_byte_buddy();

    test_case();
    test_suite();

    test_runners_run();
    test_runners_run_in();
    test_runners_thread_scoped();
    test_runners_class_loader_scoped();

    test_reports_count_throwables();
    test_reports_format();

    runTestsIn(TestQuackeryRunnerQuackeryAnnotation.class);
    runTestsIn(TestQuackeryRunnerJunitTestAnnotation.class);
    runTestsIn(TestQuackeryRunnerDescriptionHierarchy.class);
    runTestsIn(TestQuackeryRunnerIgnoreAnnotation.class);
    runTestsIn(TestQuackeryRunnerInitialization.class);

    test_collection_contract();

    long stop = System.nanoTime();
    BigDecimal time = new BigDecimal(BigInteger.valueOf(stop - start), 9).setScale(3, HALF_UP);
    System.out.println(format("finished in %s seconds", time));

    if (failures.size() == 0) {
      System.out.println("no failures");
      for (String stat : statistics) {
        System.out.println(stat);
      }
    } else {
      System.out.println(format("%s failures", failures.size()));
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
    statistics.add(format("%s : %s", type.getSimpleName(), count));
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
