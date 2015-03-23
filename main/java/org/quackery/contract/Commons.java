package org.quackery.contract;

import static java.util.Arrays.asList;

import java.lang.reflect.Constructor;
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

  public static List<Class<?>> parameters(Constructor<?> constructor) {
    return asList(constructor.getParameterTypes());
  }

  public static String print(Object[] array) {
    return Arrays.toString(array);
  }

  public static String print(Collection<?> collection) {
    return print(collection.toArray());
  }
}
