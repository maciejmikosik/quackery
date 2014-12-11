package org.testanza;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class Testers {
  public static <T> Tester<T> asTester(final Matcher<T> matcher) {
    return new CaseTester<T>() {
      protected String name(T item) {
        return item + " is " + matcher.toString();
      }

      protected void body(T item) throws Throwable {
        if (!matcher.matches(item)) {
          throw new TestanzaAssertionError("" //
              + "  expected that\n" //
              + "    " + item + "\n" //
              + "  matches\n" //
              + "    " + matcher + "\n" //
              + "  but\n" //
              + "    " + diagnose(item, matcher) + "\n" //
          );
        }
      }
    };
  }

  private static <T> String diagnose(T item, Matcher<T> matcher) {
    StringDescription description = new StringDescription();
    matcher.describeMismatch(item, description);
    return description.toString();
  }

  public static Tester<AnnotatedElement> hasModifier(final int modifier) {
    return new CaseTester<AnnotatedElement>() {
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
    return new CaseTester<AnnotatedElement>() {
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
