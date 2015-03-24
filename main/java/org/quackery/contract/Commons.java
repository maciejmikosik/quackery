package org.quackery.contract;

import static java.util.Arrays.asList;

import java.lang.reflect.Constructor;
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
}
