package org.quackery.junit;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.WRAPPER;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.quackery.Quackery;
import org.quackery.Test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.ExceptionMethod;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.StubMethod;

class JunitClassBuilder {
  private final Builder<?> builder;

  private JunitClassBuilder(Builder<?> builder) {
    this.builder = builder;
  }

  public JunitClassBuilder() {
    this(new ByteBuddy()
        .subclass(Object.class)
        .name("JunitClass")
        .annotateType(annotationRunWith(QuackeryRunner.class)));
  }

  public JunitClassBuilder(ConstructorStrategy strategy) {
    this(new ByteBuddy()
        .subclass(Object.class, strategy)
        .name("JunitClass")
        .annotateType(annotationRunWith(QuackeryRunner.class)));
  }

  public JunitClassBuilder name(String name) {
    return new JunitClassBuilder(builder.name(name));
  }

  public JunitClassBuilder annotate(Annotation annotation) {
    return new JunitClassBuilder(builder.annotateType(annotation));
  }

  public JunitClassBuilder define(MethodDefinition def) {
    return new JunitClassBuilder(builder
        .defineMethod(def.name, def.returnType, def.modifiers)
        .withParameters(def.parameters)
        .intercept(implementationOf(def))
        .annotateMethod(def.annotations));
  }

  public JunitClassBuilder defineConstructor(MethodDefinition def) {
    return new JunitClassBuilder(builder
        .defineConstructor(def.modifiers)
        .withParameters(def.parameters)
        .intercept(MethodCall.invoke(objectConstrcutor()))
        .annotateMethod(def.annotations));
  }

  private static Constructor<Object> objectConstrcutor() {
    try {
      return Object.class.getDeclaredConstructor();
    } catch (ReflectiveOperationException e) {
      throw new Error(e);
    }
  }

  private static Implementation implementationOf(MethodDefinition def) {
    return def.throwing != null
        ? ExceptionMethod.throwing(def.throwing)
        : def.returning == null
            ? def.returnType == void.class
                ? StubMethod.INSTANCE
                : FixedValue.nullValue()
            : FixedValue.reference(def.returning);
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
        .name("default_quackery_method")
        .parameters();
  }

  public static MethodDefinition defaultJunitMethod() {
    return new MethodDefinition()
        .annotations(annotationJunitTest())
        .modifiers(PUBLIC)
        .returnType(void.class)
        .name("default_junit_method")
        .parameters()
        .returning(null);
  }

  public static Annotation annotationRunWith(final Class<? extends Runner> type) {
    return new RunWith() {
      public Class<? extends Annotation> annotationType() {
        return RunWith.class;
      }

      public Class<? extends Runner> value() {
        return type;
      }
    };
  }

  public static Annotation annotationQuackery() {
    return new Quackery() {
      public Class<? extends Annotation> annotationType() {
        return Quackery.class;
      }
    };
  }

  public static Annotation annotationJunitTest() {
    return new org.junit.Test() {
      public Class<? extends Annotation> annotationType() {
        return org.junit.Test.class;
      }

      public Class<? extends Throwable> expected() {
        return None.class;
      }

      public long timeout() {
        return 0L;
      }
    };
  }

  public static Annotation annotationIgnore(final String reason) {
    return new Ignore() {
      public Class<? extends Annotation> annotationType() {
        return Ignore.class;
      }

      public String value() {
        return reason;
      }
    };
  }
}
