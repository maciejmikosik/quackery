package org.quackery.contract.collection;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static java.util.Arrays.asList;
import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.WRAPPER;
import static net.bytebuddy.implementation.MethodDelegation.toConstructor;

import java.util.Collection;

import net.bytebuddy.ByteBuddy;

public class Factories {
  public static Class<?> asFactory(Class<?> returnType, String methodName, Class<?> delegate) {
    return new ByteBuddy()
        .subclass(Object.class)
        .name("FactoryFor" + delegate.getSimpleName())
        .defineMethod(methodName, returnType, asList(Collection.class), PUBLIC | STATIC)
        .intercept(toConstructor(delegate))
        .make()
        .load(Thread.currentThread().getContextClassLoader(), WRAPPER)
        .getLoaded();
  }
}
