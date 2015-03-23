package org.quackery.testing.bug;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.quackery.testing.bug.collect.MutableList;

public class Bugs {
  private static final List<Class<?>> implementations = implementations();

  private static List<Class<?>> implementations() {
    List<Class<?>> allImplementations = new ArrayList<>();
    allImplementations.add(MutableList.class);
    return unmodifiableList(allImplementations);
  }

  public static List<Class<?>> bugs(Class<?>... contract) {
    List<Class<?>> bugs = new ArrayList<>();
    for (Class<?> implementation : implementations) {
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
}
