package org.quackery.testing.bug;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.quackery.testing.bug.collect.MutableList;

public class Bugs {
  private static final List<Class<?>> universe = universe();

  private static List<Class<?>> universe() {
    List<Class<?>> implementations = new ArrayList<>();
    implementations.add(MutableList.class);
    return unmodifiableList(implementations);
  }

  public static List<Class<?>> implementations(Class<?>... contract) {
    List<Class<?>> implementations = new ArrayList<>();
    for (Class<?> implementation : universe) {
      if (isAssignable(implementation, contract)) {
        implementations.add(implementation);
      }
    }
    return unmodifiableList(implementations);
  }

  public static List<Class<?>> bugs(Class<?>... contract) {
    List<Class<?>> bugs = new ArrayList<>();
    for (Class<?> implementation : universe) {
      for (Class<?> bug : implementation.getDeclaredClasses()) {
        if (unorderedEquals(bug.getAnnotation(Bug.class).value(), contract)) {
          bugs.add(bug);
        }
      }
    }
    return bugs;
  }

  private static <T> boolean unorderedEquals(T[] arrayA, T[] arrayB) {
    List<T> listA = asList(arrayA);
    List<T> listB = asList(arrayB);
    return listA.containsAll(listB) && listB.containsAll(listA);
  }

  private static boolean isAssignable(Class<?> subclass, Class<?>... superclasses) {
    for (Class<?> superclass : superclasses) {
      if (!superclass.isAssignableFrom(subclass)) {
        return false;
      }
    }
    return true;
  }
}
