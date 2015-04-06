package org.quackery.contract.collection;

import static org.quackery.AssumptionException.assume;

import java.lang.reflect.Method;
import java.util.Collection;

import org.quackery.AssumptionException;

public class FactoryCreator implements Creator {
  private final Class<?> type;
  private final String methodName;

  public FactoryCreator(Class<?> type, String methodName) {
    this.type = type;
    this.methodName = methodName;
  }

  public <T> T create(Class<T> cast, Object original) throws ReflectiveOperationException {
    try {
      Method method = type.getMethod(methodName, Collection.class);
      assume(cast.isAssignableFrom(method.getReturnType()));
      return cast.cast(method.invoke(null, original));
    } catch (NoSuchMethodException e) {
      throw new AssumptionException();
    }
  }
}
