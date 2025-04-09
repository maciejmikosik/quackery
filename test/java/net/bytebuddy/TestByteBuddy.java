package net.bytebuddy;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.reflect.Modifier.PUBLIC;
import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.WRAPPER;
import static org.quackery.testing.Testing.assertTrue;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;

import net.bytebuddy.implementation.SuperMethodCall;

public class TestByteBuddy {
  public static void test_byte_buddy() throws Exception {
    can_redefine_inner_class_as_public_with_default_constructor();
    redefining_class_preserves_annotation_on_methods();
  }

  private static void can_redefine_inner_class_as_public_with_default_constructor() throws Exception {
    Class<?> type = new ByteBuddy()
        .redefine(Inner.class)
        .modifiers(PUBLIC)
        .defineConstructor(PUBLIC)
        .intercept(SuperMethodCall.INSTANCE)
        .name(TestByteBuddy.class.getPackageName() + ".Redefined")
        .make()
        .load(Thread.currentThread().getContextClassLoader(), WRAPPER)
        .getLoaded();

    assertTrue(type.getConstructor().newInstance() != null);
    assertTrue(Modifier.isPublic(type.getModifiers()));
  }

  private class Inner {}

  private static void redefining_class_preserves_annotation_on_methods() throws Exception {
    class TestClass {
      @TestAnnotation
      void method() {}
    }

    Class<?> type = new ByteBuddy()
        .redefine(TestClass.class)
        .name(TestByteBuddy.class.getPackageName() + ".Redefined")
        .make()
        .load(Thread.currentThread().getContextClassLoader(), WRAPPER)
        .getLoaded();

    assertTrue(type.getDeclaredMethod("method").isAnnotationPresent(TestAnnotation.class));
  }

  @Retention(RUNTIME)
  private @interface TestAnnotation {}
}
