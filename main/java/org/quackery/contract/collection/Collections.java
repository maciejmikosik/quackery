package org.quackery.contract.collection;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;

public class Collections {
  public static <T, E extends T> ArrayList<T> newArrayList(E... elements) {
    return new ArrayList<T>(asList(elements));
  }

  public static <E> ArrayList<E> copy(ArrayList<E> list) {
    return list == null
        ? null
        : (ArrayList<E>) list.clone();
  }

  public static <E> E[] copy(E[] array) {
    return array == null
        ? null
        : Arrays.copyOf(array, array.length);
  }
}
