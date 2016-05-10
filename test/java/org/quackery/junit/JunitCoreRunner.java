package org.quackery.junit;

import static net.bytebuddy.description.modifier.Ownership.STATIC;
import static net.bytebuddy.description.modifier.Visibility.PUBLIC;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;

import java.lang.reflect.Modifier;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.quackery.Test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.SuperMethodCall;

class JunitCoreRunner {
  public static Result run(Class<?> type) {
    return new JUnitCore().run(asNotInner(type));
  }

  private static Class<?> asNotInner(Class<?> type) {
    return isInner(type)
        ? asNested(type)
        : type;
  }

  private static boolean isInner(Class<?> type) {
    return type.getEnclosingClass() != null && !Modifier.isStatic(type.getModifiers());
  }

  private static Class<?> asNested(Class<?> inner) {
    return new ByteBuddy()
        .redefine(inner)
        .modifiers(PUBLIC, STATIC)
        .defineConstructor(PUBLIC)
        .intercept(SuperMethodCall.INSTANCE)
        .name(inner.getName() + "$$Nested")
        .make()
        .load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.INJECTION)
        .getLoaded();
  }

  public static Throwable runFailing(Class<?> type) {
    return run(type).getFailures().get(0).getException();
  }

  public static Result run(Test test) {
    return run(new JunitClassBuilder()
        .define(defaultQuackeryMethod().returning(test))
        .load());
  }
}
