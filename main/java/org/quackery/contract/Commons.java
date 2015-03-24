package org.quackery.contract;

import static java.util.Arrays.asList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

import org.quackery.AssumptionException;

public class Commons {
  public static Constructor<?> assumeConstructor(Class<?> type, Class<?>... parameters) {
    try {
      return type.getConstructor(parameters);
    } catch (NoSuchMethodException e) {
      throw new AssumptionException(e);
    }
  }

  public static <T, E extends T> ArrayList<T> newArrayList(E... elements) {
    return new ArrayList<T>(asList(elements));
  }

  public static <E> ArrayList<E> copy(ArrayList<E> list) {
    return list == null
        ? null
        : (ArrayList<E>) list.clone();
  }

  public static <E> E[] copy(E... array) {
    return array == null
        ? null
        : Arrays.copyOf(array, array.length);
  }
}
