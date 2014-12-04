package org.testanza.testers;

import static java.lang.System.identityHashCode;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import org.testanza.BodyTester;
import org.testanza.Tester;

public class TestersForMembers {
  public static Tester<Member> hasModifier(final int modifier) {
    return new BodyTester<Member>() {
      protected String name(Member member) {
        return type(member) + " " + simpleName(member) + " has modifier "
            + Modifier.toString(modifier) + " #" + identityHashCode(member);
      }

      protected void body(Member member) throws Throwable {
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
