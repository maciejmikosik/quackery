package org.testanza;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;

public class TestMembers {
  public static Tester<Member> hasModifier(final int modifier) {
    return new Tester<Member>() {
      public Test test(final Member member) {
        return new TestCase(formatTestThat(member, "hasModifier", formatModifier(modifier))) {
          protected void runTest() {
            assertTrue((member.getModifiers() & modifier) != 0);
          }
        };
      }
    };
  }

  private static String formatTestThat(Object item, String method, Object... arguments) {
    return "testThat(" + String.valueOf(item) + ", " + method + "(" + join(arguments) + "))";
  }

  private static String join(Object... objects) {
    String string = Arrays.asList(objects).toString();
    return string.substring(1, string.length() - 1);
  }

  private static String formatModifier(final int modifier) {
    return Modifier.toString(modifier).toUpperCase();
  }
}
