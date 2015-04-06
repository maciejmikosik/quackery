package org.quackery.contract.collection;

import java.lang.reflect.Constructor;

import org.quackery.AssumptionException;

public class Assumptions {
  public static Constructor<?> assumeConstructor(Class<?> type, Class<?>... parameters) {
    try {
      return type.getConstructor(parameters);
    } catch (NoSuchMethodException e) {
      throw new AssumptionException(e);
    }
  }

}
