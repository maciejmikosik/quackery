package org.quackery.junit;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.WRAPPER;
import static net.bytebuddy.implementation.FixedValue.reference;

import java.lang.annotation.Annotation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;

import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.quackery.Quackery;
import org.quackery.Test;

class JunitClassBuilder {
  public final Builder<?> builder;

  private JunitClassBuilder(Builder<?> builder) {
    this.builder = builder;
  }

  public JunitClassBuilder() {
    this(new ByteBuddy()
        .subclass(Object.class)
        .name("JunitClass")
        .annotateType(annotationRunWith(QuackeryRunner.class)));
  }

  public JunitClassBuilder define(MethodDefinition def) {
    return new JunitClassBuilder(builder
        .defineMethod(def.name, def.returnType, def.parameters, def.modifiers)
        .intercept(reference(def.returns))
        .annotateMethod(def.annotations));
  }

  public Class<?> load() {
    return builder
        .make()
        .load(Thread.currentThread().getContextClassLoader(), WRAPPER)
        .getLoaded();
  }

  public static MethodDefinition defaultQuackeryMethod() {
    return new MethodDefinition()
        .annotations(annotationQuackery())
        .modifiers(PUBLIC | STATIC)
        .returnType(Test.class)
        .name("test")
        .parameters();
  }

  private static Annotation annotationRunWith(final Class<? extends Runner> type) {
    return new RunWith() {
      public Class<? extends Annotation> annotationType() {
        return RunWith.class;
      }

      public Class<? extends Runner> value() {
        return type;
      }
    };
  }

  private static Annotation annotationQuackery() {
    return new Quackery() {
      public Class<? extends Annotation> annotationType() {
        return Quackery.class;
      }
    };
  }
}
