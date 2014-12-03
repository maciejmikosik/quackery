package org.testanza;

import static java.lang.System.identityHashCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestCase;

public class TestMembers {
  public static Tester<Member> hasModifier(final int modifier) {
    return new Tester<Member>() {
      public Test test(final Member member) {
        String testName = type(member) + " " + simpleName(member) + " has modifier "
            + Modifier.toString(modifier) + " #" + identityHashCode(member);
        return new TestCase(testName) {
          protected void runTest() {
            if (!hasModifier(modifier, member)) {
              fail("" //
                  + "\n" //
                  + "  expected that\n" //
                  + "    " + type(member) + " " + member.toString() + "\n" //
                  + "  has modifier\n" //
                  + "    " + Modifier.toString(modifier) + "\n" //
              );
            }
          }
        };
      }
    };
  }

  private static boolean hasModifier(int modifier, final Member member) {
    return (member.getModifiers() & modifier) != 0;
  }

  private static String type(Member member) {
    return member.getClass().getSimpleName().toLowerCase();
  }

  private static String simpleName(Member member) {
    return member instanceof Constructor
        ? member.getDeclaringClass().getSimpleName()
        : member.getName();
  }
}
