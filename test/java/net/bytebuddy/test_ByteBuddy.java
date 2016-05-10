package net.bytebuddy;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static net.bytebuddy.description.modifier.Ownership.STATIC;
import static net.bytebuddy.description.modifier.Visibility.PUBLIC;
import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.INJECTION;
import static org.quackery.testing.Assertions.assertTrue;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;

import net.bytebuddy.implementation.SuperMethodCall;

public class test_ByteBuddy {
  public void can_redefine_inner_class_as_nested_public() throws Exception {
    class Inner {}

    // when
    Class<?> type = new ByteBuddy()
        .redefine(Inner.class)
        .modifiers(PUBLIC, STATIC)
        .defineConstructor(PUBLIC)
        .intercept(SuperMethodCall.INSTANCE)
        .name(Inner.class.getName() + "$$Nested")
        .make()
        .load(Thread.currentThread().getContextClassLoader(), INJECTION)
        .getLoaded();

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
    Class<?> type = new ByteBuddy()
        .redefine(TestClass.class)
        .name(TestClass.class.getName() + "$$Redefined")
        .make()
        .load(Thread.currentThread().getContextClassLoader(), INJECTION)
        .getLoaded();

    // then
    assertTrue(type.getDeclaredMethod("method").isAnnotationPresent(TestAnnotation.class));
  }

  @Retention(RUNTIME)
  private @interface TestAnnotation {}
}
