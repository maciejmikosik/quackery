package org.quackery.contract.collection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.quackery.AssumptionException;

public class Factories {
  public static ThreadLocal<Class<?>> target = new ThreadLocal<>();

  public static Class<?> asCollectionFactory(Class<?> collectionWithConstructor) {
    target.set(collectionWithConstructor);
    return CollectionFactory.class;
  }

  public static Class<?> asListFactory(Class<?> collectionWithConstructor) {
    target.set(collectionWithConstructor);
    return ListFactory.class;
  }

  public static class CollectionFactory {
    public static Collection<?> create(Collection<?> collection) throws Throwable {
      return (Collection<?>) createCollection(collection);
    }
  }

  public static class ListFactory {
    public static List<?> create(Collection<?> collection) throws Throwable {
      return (List<?>) createCollection(collection);
    }
  }

  private static Object createCollection(Collection<?> collection) throws Throwable {
    try {
      Constructor<?> constructor = target.get().getConstructor(Collection.class);
      return constructor.newInstance(collection);
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
      throw new AssumptionException(e);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
