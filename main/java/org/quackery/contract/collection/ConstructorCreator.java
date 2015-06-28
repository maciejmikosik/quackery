package org.quackery.contract.collection;

import static org.quackery.AssumptionException.assume;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.quackery.AssumptionException;

public class ConstructorCreator implements Creator {
  private final Class<?> type;

  public ConstructorCreator(Class<?> type) {
    this.type = type;
  }

  public <T> T create(Class<T> cast, Object original) throws Throwable {
    assume(cast.isAssignableFrom(type));
    try {
      Constructor<?> constructor = type.getConstructor(Collection.class);
      return cast.cast(constructor.newInstance(original));
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
      throw new AssumptionException(e);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
