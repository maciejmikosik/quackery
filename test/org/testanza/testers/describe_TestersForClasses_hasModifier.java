package org.testanza.testers;

import static org.testanza.describe_testanza.verify;
import static org.testanza.testers.TestersForClasses.hasModifier;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestCase;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class describe_TestersForClasses_hasModifier {
  private static AnnotatedElement item;
  private static Test test;
  private static Result result;
  private static String name, message;

  public static void succeeds_if_method_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      final void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);
    result = new JUnitCore().run(test);
    verify(0 == result.getFailureCount());
  }

  public static void fails_if_method_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);
    result = new JUnitCore().run(test);
    verify(1 == result.getFailureCount());
  }

  public static void succeeds_if_class_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    final class TestClass {
      final void testMethod() {}
    }
    test = hasModifier(Modifier.FINAL).test(TestClass.class);
    result = new JUnitCore().run(test);
    verify(0 == result.getFailureCount());
  }

  public static void fails_if_class_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    test = hasModifier(Modifier.FINAL).test(TestClass.class);
    result = new JUnitCore().run(test);
    verify(1 == result.getFailureCount());
  }

  public static void failure_prints_message() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void foo() {}
    }
    item = TestClass.class.getDeclaredMethod("foo");
    test = hasModifier(Modifier.FINAL).test(item);
    result = new JUnitCore().run(test);

    message = result.getFailures().get(0).getMessage();
    verify(message.contains("" //
        + "\n" //
        + "  expected that\n" //
        + "    method " + item.toString() + "\n" //
        + "  has modifier\n" //
        + "    final\n" //
    ));
  }

  public static void test_name_contains_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);
    name = ((TestCase) test).getName();
    verify(name.contains("final"));
  }

  public static void test_name_contains_member_type_and_simple_name() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      TestClass() {}

      Object foo;

      void foo() {}
    }
    item = TestClass.class.getDeclaredMethod("foo");
    test = hasModifier(Modifier.FINAL).test(item);
    name = ((TestCase) test).getName();
    verify(name.contains("method foo"));

    item = TestClass.class.getDeclaredField("foo");
    test = hasModifier(Modifier.FINAL).test(item);
    name = ((TestCase) test).getName();
    verify(name.contains("field foo"));

    item = TestClass.class.getDeclaredConstructor();
    test = hasModifier(Modifier.FINAL).test(item);
    name = ((TestCase) test).getName();
    verify(name.contains("constructor TestClass"));

    item = TestClass.class;
    test = hasModifier(Modifier.FINAL).test(item);
    name = ((TestCase) test).getName();
    verify(name.contains("class TestClass"));
  }
}
