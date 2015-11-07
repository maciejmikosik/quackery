package org.quackery.contract.collection;

import static org.quackery.report.AssumeException.assume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.quackery.report.AssumeException;

public class FactoryCreator implements Creator {
  private final Class<?> type;
  private final String methodName;

  public FactoryCreator(Class<?> type, String methodName) {
    this.type = type;
    this.methodName = methodName;
  }

  public <T> T create(Class<T> cast, Object original) throws Throwable {
    try {
      Method method = type.getMethod(methodName, Collection.class);
      assume(cast.isAssignableFrom(method.getReturnType()));
      assume(Modifier.isStatic(method.getModifiers()));
      return cast.cast(method.invoke(null, original));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AssumeException(e);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
