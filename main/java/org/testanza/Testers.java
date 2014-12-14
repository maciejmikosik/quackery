package org.testanza;

import static java.util.Arrays.asList;
import static java.util.Objects.deepEquals;
import static org.testanza.Case.newCase;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class Testers {
  public static <T> Tester<T> asTester(final Matcher<T> matcher) {
    return new Tester<T>() {
      public Test test(final T item) {
        String name = item + " is " + matcher.toString();
        Closure body = new Closure() {
          public void invoke() throws Throwable {
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
        return newCase(name, body);
      }
    };
  }

  private static <T> String diagnose(T item, Matcher<T> matcher) {
    StringDescription description = new StringDescription();
    matcher.describeMismatch(item, description);
    return description.toString();
  }

  public static Tester<AnnotatedElement> hasModifier(final int modifier) {
    return new Tester<AnnotatedElement>() {
      public Test test(final AnnotatedElement element) {
        String name = kind(element) + " " + simpleName(element) + " has modifier "
            + Modifier.toString(modifier);
        Closure body = new Closure() {
          public void invoke() throws Throwable {
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
        return newCase(name, body);
      }
    };
  }

  public static Tester<AnnotatedElement> hasNoModifier(final int modifier) {
    return new Tester<AnnotatedElement>() {
      public Test test(final AnnotatedElement element) {
        String name = kind(element) + " " + simpleName(element) + " has no modifier "
            + Modifier.toString(modifier);
        Closure body = new Closure() {
          public void invoke() throws Throwable {
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
        return newCase(name, body);
      }
    };
  }

  public static Tester<Class<?>> hasConstructor(final int modifier, final Class<?>... parameters) {
    return new Tester<Class<?>>() {
      public Test test(final Class<?> type) {
        String name = "class " + type.getSimpleName() + " has " + Modifier.toString(modifier)
            + " constructor with " + parameters.length + " parameters "
            + printParameters(parameters);
        Closure body = new Closure() {
          public void invoke() throws Throwable {
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
              if (hasModifier(modifier, constructor)
                  && deepEquals(constructor.getParameterTypes(), parameters)) {
                return;
              }
            }
            throw new TestanzaAssertionError("" //
                + "  expected that\n" //
                + "    " + type + "\n" //
                + "  has constructor with modifier\n" //
                + "    " + Modifier.toString(modifier) + "\n" //
                + "  and " + parameters.length + " parameters\n" //
                + printLines("    ", asList(parameters)) //
            );
          }
        };
        return newCase(name, body);
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

  private static String printLines(String prefix, List<?> objects) {
    StringBuilder builder = new StringBuilder();
    for (Object object : objects) {
      builder.append(prefix).append(String.valueOf(object)).append("\n");
    }
    return builder.toString();
  }

  private static String printParameters(Class<?>[] parameters) {
    StringBuilder builder = new StringBuilder();
    for (Class<?> parameter : parameters) {
      builder.append(parameter.getSimpleName()).append(", ");
    }
    if (parameters.length > 0) {
      builder.delete(builder.length() - 2, builder.length());
    }
    return builder.toString();
  }
}
