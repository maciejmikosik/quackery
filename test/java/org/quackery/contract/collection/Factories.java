package org.quackery.contract.collection;

import java.lang.reflect.Constructor;
import java.util.Collection;

public class Factories {
  public static ThreadLocal<Class<?>> target = new ThreadLocal<>();

  public static Class<?> asFactory(Class<?> collectionWithConstructor) {
    Factories.target.set(collectionWithConstructor);
    return CollectionFactory.class;
  }

  public static class CollectionFactory {
    public static Collection<?> create(Collection<?> collection) {
      try {
        Constructor<?> constructor = target.get().getConstructor(Collection.class);
        return (Collection<?>) constructor.newInstance(collection);
      } catch (ReflectiveOperationException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
