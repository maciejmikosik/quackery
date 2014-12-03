package org.testanza;

import static org.testanza.TestMembers.hasModifier;
import static org.testanza.describe_testanza.verify;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestCase;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class describe_TestMembers_has_modifier {
  private static Method member;
  private static Test test;
  private static Result result;
  private static String name;

  public static void succeeds_if_method_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      final void testMethod() {}
    }
    member = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(member);
    result = new JUnitCore().run(test);
    verify(0 == result.getFailureCount());
  }

  public static void fails_if_method_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    member = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(member);
    result = new JUnitCore().run(test);
    verify(1 == result.getFailureCount());
  }

  public static void test_name_contains_member_and_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    member = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(member);
    name = ((TestCase) test).getName();
    verify(name.contains("FINAL"));
    verify(name.contains(member.toString()));
  }
}
