package org.quackery.contract;

import static java.util.Arrays.asList;
import static org.quackery.AssumptionException.assume;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.quackery.AssumptionException;

public class Commons {
  public static Constructor<?> assumeConstructor(Class<?> type, Class<?>... parameters) {
    try {
      return type.getConstructor(parameters);
    } catch (NoSuchMethodException e) {
      throw new AssumptionException(e);
    }
  }

  public static Collection<Object> assumeCreateCollection(Class<?> type, Collection<?> original)
      throws ReflectiveOperationException {
    assume(Collection.class.isAssignableFrom(type));
    Constructor<?> constructor = assumeConstructor(type, Collection.class);
    return (Collection<Object>) constructor.newInstance(original);
  }

  public static List<Object> assumeCreateList(Class<?> type, Collection<?> original)
      throws ReflectiveOperationException {
    assume(List.class.isAssignableFrom(type));
    Constructor<?> constructor = assumeConstructor(type, Collection.class);
    return (List<Object>) constructor.newInstance(original);
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
