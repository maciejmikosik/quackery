package org.quackery.contract.collection;

import static org.quackery.AssumptionException.assume;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

import org.quackery.AssumptionException;

public class Assumptions {
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
}
