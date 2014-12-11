package org.testanza.testers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import org.testanza.BodyTester;
import org.testanza.TestanzaAssertionError;
import org.testanza.Tester;

public class TestersForClasses {
  public static Tester<AnnotatedElement> hasModifier(final int modifier) {
    return new BodyTester<AnnotatedElement>() {
      protected String name(AnnotatedElement element) {
        return kind(element) + " " + simpleName(element) + " has modifier "
            + Modifier.toString(modifier);
      }

      protected void body(AnnotatedElement element) throws Throwable {
        if (!hasModifier(modifier, element)) {
          throw new TestanzaAssertionError("" //
              + "\n" //
              + "  expected that\n" //
              + "    " + fullName(element) + "\n" //
              + "  has modifier\n" //
              + "    " + Modifier.toString(modifier) + "\n" //
          );
        }
      }
    };
  }

  public static Tester<AnnotatedElement> hasNoModifier(final int modifier) {
    return new BodyTester<AnnotatedElement>() {
      protected String name(AnnotatedElement element) {
        return kind(element) + " " + simpleName(element) + " has no modifier "
            + Modifier.toString(modifier);
      }

      protected void body(AnnotatedElement element) throws Throwable {
        if (hasModifier(modifier, element)) {
          throw new TestanzaAssertionError("" //
              + "\n" //
              + "  expected that\n" //
              + "    " + fullName(element) + "\n" //
              + "  has no modifier\n" //
              + "    " + Modifier.toString(modifier) + "\n" //
          );
        }
      }
    };
  }

  private static boolean hasModifier(int modifier, AnnotatedElement element) {
    return (modifiers(element) & modifier) != 0;
  }

  private static int modifiers(AnnotatedElement element) {
    if (element instanceof Class<?>) {
      return ((Class<?>) element).getModifiers();
    } else if (element instanceof Member) {
      return ((Member) element).getModifiers();
    } else {
      throw new RuntimeException("unknown element " + element);
    }
  }

  private static String kind(AnnotatedElement element) {
    return element.getClass().getSimpleName().toLowerCase();
  }

  private static String simpleName(AnnotatedElement element) {
    if (element instanceof Class<?>) {
      return ((Class<?>) element).getSimpleName();
    } else if (element instanceof Constructor) {
      return ((Constructor<?>) element).getDeclaringClass().getSimpleName();
    } else if (element instanceof Member) {
      return ((Member) element).getName();
    } else {
      throw new RuntimeException("unknown element " + element);
    }
  }

  private static String fullName(AnnotatedElement element) {
    if (element instanceof Class<?>) {
      return element.toString();
    } else {
      return kind(element) + " " + element.toString();
    }
  }
}
