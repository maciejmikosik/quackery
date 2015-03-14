package org.quackery.common;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;

public class Classes {
  public static int modifiers(AnnotatedElement element) {
    if (element instanceof Class<?>) {
      return ((Class<?>) element).getModifiers();
    } else if (element instanceof Member) {
      return ((Member) element).getModifiers();
    } else {
      throw new RuntimeException("unknown element " + element);
    }
  }

  public static String kind(AnnotatedElement element) {
    return element.getClass().getSimpleName().toLowerCase();
  }

  public static String simpleName(AnnotatedElement element) {
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

  public static String fullName(AnnotatedElement element) {
    if (element instanceof Class<?>) {
      return element.toString();
    } else {
      return kind(element) + " " + element.toString();
    }
  }
}
