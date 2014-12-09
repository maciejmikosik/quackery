package org.testanza;

import static java.util.Arrays.asList;
import static org.testanza.Case.newCase;
import static org.testanza.Junit.junit;
import static org.testanza.Suite.newSuite;
import static org.testanza.Testilities.newClosure;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;
import static org.testanza.Testilities.verifyNotEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class describe_Junit_junit {
  private final RuntimeException exception = new RuntimeException("exception");
  private final String name = "name " + hashCode();
  private final Closure body = newClosure("body");
  private boolean invoked;
  private Test test;
  private junit.framework.Test junitTest, otherJunitTest;
  private List<junit.framework.Test> junitTests;

  public void copies_case_name() {
    test = newCase(name, body);
    junitTest = junit(test);
    verifyEquals(name(junitTest), name);
  }

  public void running_test_invokes_case_body() throws Throwable {
    test = newCase(name, new Closure() {
      public void invoke() {
        invoked = true;
      }
    });
    junitTest = junit(test);

    run(junitTest);

    verifyEquals(invoked, true);
  }

  public void running_test_throws_exception_from_case_body() throws Throwable {
    test = newCase(name, new Closure() {
      public void invoke() throws Throwable {
        throw exception;
      }
    });
    junitTest = junit(test);

    try {
      run(junitTest);
      verifyFail();
    } catch (RuntimeException e) {
      verifyEquals(e, exception);
    }
  }

  public void copies_suite_name() {
    test = newSuite(name, Arrays.<Test> asList());

    junitTest = junit(test);

    verifyEquals(((TestSuite) junitTest).getName(), name);
  }

  public void copies_hierarchy() throws Throwable {
    test = newSuite("", asList(newCase(name, new Closure() {
      public void invoke() throws Throwable {
        invoked = true;
      }
    })));

    junitTest = junit(test);

    junitTests = enumerate(junitTest);
    verifyEquals(junitTests.size(), 1);
    verifyEquals(name(junitTests.get(0)), name);

    run(junitTests.get(0));
    verifyEquals(invoked, true);
  }

  public void renames_colliding_names() {
    test = newSuite("suite", asList(newCase(name, body), newCase(name, body)));

    junitTest = junit(test);

    junitTests = enumerate(junitTest);
    verifyNotEquals(name(junitTests.get(0)), name(junitTests.get(1)));
  }

  public void renames_colliding_names_from_different_conversions() {
    junitTest = junit(newCase(name, body));
    otherJunitTest = junit(newCase(name, body));

    verifyNotEquals(name(junitTest), name(otherJunitTest));
  }

  private static String name(junit.framework.Test junitTest) {
    return junitTest instanceof TestCase
        ? ((TestCase) junitTest).getName()
        : ((TestSuite) junitTest).getName();
  }

  private static void run(junit.framework.Test junitTest) throws Throwable {
    try {
      Method method = TestCase.class.getDeclaredMethod("runTest");
      method.setAccessible(true);
      method.invoke(junitTest);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    }
  }

  private static List<junit.framework.Test> enumerate(junit.framework.Test junitTest) {
    Enumeration<junit.framework.Test> enumeration = ((TestSuite) junitTest).tests();
    List<junit.framework.Test> tests = new ArrayList<junit.framework.Test>();
    while (enumeration.hasMoreElements()) {
      tests.add(enumeration.nextElement());
    }
    return tests;
  }
}
