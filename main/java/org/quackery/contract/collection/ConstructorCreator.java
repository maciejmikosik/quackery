package org.quackery.contract.collection;

import static org.quackery.AssumptionException.assume;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.quackery.AssumptionException;

public class ConstructorCreator implements Creator {
  private final Class<?> type;

  public ConstructorCreator(Class<?> type) {
    this.type = type;
  }

  public <T> T create(Class<T> cast, Object original) throws ReflectiveOperationException {
    assume(cast.isAssignableFrom(type));
    try {
      Constructor<?> constructor = type.getConstructor(Collection.class);
      return cast.cast(constructor.newInstance(original));
    } catch (NoSuchMethodException e) {
      throw new AssumptionException();
    }
  }
}
