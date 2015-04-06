package org.quackery.contract.collection;

import static org.quackery.AssumptionException.assume;
import static org.quackery.contract.collection.Assumptions.assumeConstructor;

import java.lang.reflect.Constructor;
import java.util.Collection;

public class ConstructorCreator implements Creator {
  private final Class<?> type;

  public ConstructorCreator(Class<?> type) {
    this.type = type;
  }

  public <T> T create(Class<T> cast, Object original) throws ReflectiveOperationException {
    assume(cast.isAssignableFrom(type));
    Constructor<?> constructor = assumeConstructor(type, Collection.class);
    return cast.cast(constructor.newInstance(original));
  }
}
