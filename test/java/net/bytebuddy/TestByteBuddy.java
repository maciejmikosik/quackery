package net.bytebuddy;

import static java.lang.reflect.Modifier.PUBLIC;
import static org.quackery.testing.Assertions.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Modifier;

import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.SuperMethodCall;

public class TestByteBuddy {
  public void can_redefine_inner_class_as_public_with_default_constructor() throws Exception {
    class Inner {}

    // when
    Class<?> type = load(new ByteBuddy()
        .redefine(Inner.class)
        .modifiers(PUBLIC)
        .defineConstructor(PUBLIC)
        .intercept(SuperMethodCall.INSTANCE));

    // then
    assertTrue(type.getConstructor().newInstance() != null);
    assertTrue(Modifier.isPublic(type.getModifiers()));
  }

  public void redefining_class_preserves_annotation_on_methods() throws Exception {
    class TestClass {
      @TestAnnotation
      void method() {}
    }

    // when
    Class<?> type = load(new ByteBuddy().redefine(TestClass.class));

    // then
    assertTrue(type.getDeclaredMethod("method").isAnnotationPresent(TestAnnotation.class));
  }

  private static Class<?> load(Builder<?> builder) {
    return builder
        .name("Redefined")
        .make()
        .load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
        .getLoaded();
  }

  @Retention(RetentionPolicy.RUNTIME)
  private @interface TestAnnotation {}
}
