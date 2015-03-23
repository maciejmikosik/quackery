package org.quackery;

import static java.util.Arrays.asList;
import static java.util.Objects.deepEquals;
import static org.quackery.QuackeryException.check;
import static org.quackery.common.Classes.fullName;
import static org.quackery.common.Classes.kind;
import static org.quackery.common.Classes.modifiers;
import static org.quackery.common.Classes.simpleName;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

public class Testers {
  public static Tester<AnnotatedElement> hasModifier(final int modifier) {
    return new Tester<AnnotatedElement>() {
      public Test test(final AnnotatedElement element) {
        return new Case(kind(element) + " " + simpleName(element) + " has modifier "
            + Modifier.toString(modifier)) {
          public void run() {
            if (!hasModifier(modifier, element)) {
              throw new FailureException("" //
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
    };
  }

  public static Tester<AnnotatedElement> hasNoModifier(final int modifier) {
    return new Tester<AnnotatedElement>() {
      public Test test(final AnnotatedElement element) {
        return new Case(kind(element) + " " + simpleName(element) + " has no modifier "
            + Modifier.toString(modifier)) {
          public void run() {
            if (hasModifier(modifier, element)) {
              throw new FailureException("" //
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
    };
  }

  public static Tester<Class<?>> hasConstructor(final int modifier, final Class<?>... parameters) {
    check(parameters != null);
    check(!asList(parameters).contains(null));
    return new Tester<Class<?>>() {
      public Test test(final Class<?> type) {
        return new Case("class " + type.getSimpleName() + " has " + Modifier.toString(modifier)
            + " constructor with " + parameters.length + " parameters "
            + printParameters(parameters)) {
          public void run() {
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
              if (hasModifier(modifier, constructor)
                  && deepEquals(constructor.getParameterTypes(), parameters)) {
                return;
              }
            }
            throw new FailureException("" //
                + "\n" //
                + "  expected that\n" //
                + "    " + type + "\n" //
                + "  has constructor with modifier\n" //
                + "    " + Modifier.toString(modifier) + "\n" //
                + "  and " + parameters.length + " parameters\n" //
                + printLines("    ", asList(parameters)) //
            );
          }
        };
      }
    };
  }

  public static Tester<Class<?>> isAssignableTo(final Class<?> type) {
    return new Tester<Class<?>>() {
      public Test test(final Class<?> item) {
        return new Case(simpleName(item) + " is assignable to " + simpleName(type)) {
          public void run() {
            if (!type.isAssignableFrom(item)) {
              throw new FailureException("" //
                  + "\n" //
                  + "  expected that\n" //
                  + "    " + item + "\n" //
                  + "  is assignable to\n" //
                  + "    " + type + "\n" //
              );
            }
          }
        };
      }
    };
  }

  private static boolean hasModifier(int modifier, AnnotatedElement element) {
    return (modifiers(element) & modifier) != 0;
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
