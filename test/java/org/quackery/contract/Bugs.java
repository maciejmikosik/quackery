package org.quackery.contract;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class Bugs {
  public static List<Class<?>> bugs(Class<?> model, Class<?>... contract) {
    List<Class<?>> bugs = new ArrayList<>();
    for (Class<?> bug : model.getDeclaredClasses()) {
      if (unorderedEquals(bug.getAnnotation(Bug.class).value(), contract)) {
        bugs.add(bug);
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
