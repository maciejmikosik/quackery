package org.quackery.run;

import static org.quackery.Case.newCase;
import static org.quackery.run.Runners.classLoaderScoped;
import static org.quackery.run.TestingDecorators.decorator_preserves_case_result;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_runs_cases_lazily;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.assertNotEquals;
import static org.quackery.testing.Testing.mockCase;
import static org.quackery.testing.Testing.runAndThrow;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.quackery.Test;

public class TestRunnersClassLoaderScoped {
  public static void test_runners_class_loader_scoped() throws Throwable {
    Function<Test, Test> decorator = test -> classLoaderScoped(test);

    decorator_preserves_names_and_structure(decorator);
    decorator_preserves_case_result(decorator);
    decorator_validates_arguments(decorator);
    decorator_runs_cases_lazily(decorator);

    Thread thread = Thread.currentThread();
    ClassLoader original = thread.getContextClassLoader();
    scope_is_not_context_classloader();
    thread.setContextClassLoader(original);
    scope_is_not_current_classloader();
    thread.setContextClassLoader(original);
    scope_is_child_of_context_classloader();
    thread.setContextClassLoader(original);
    restores_context_class_loader_if_successful();
    thread.setContextClassLoader(original);
    restores_context_class_loader_if_failed();
    thread.setContextClassLoader(original);
  }

  private static void scope_is_not_context_classloader() throws Throwable {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    AtomicReference<ClassLoader> scope = new AtomicReference<ClassLoader>();
    Test test = classLoaderScoped(newCase("name", () -> {
      scope.set(Thread.currentThread().getContextClassLoader());
    }));

    runAndThrow(test);

    assertNotEquals(scope.get(), original);
  }

  private static void scope_is_not_current_classloader() throws Throwable {
    ClassLoader original = TestRunnersThreadScoped.class.getClassLoader();
    AtomicReference<ClassLoader> scope = new AtomicReference<ClassLoader>();
    Test test = classLoaderScoped(newCase("name", () -> {
      scope.set(Thread.currentThread().getContextClassLoader());
    }));

    runAndThrow(test);

    assertNotEquals(scope.get(), original);
  }

  private static void scope_is_child_of_context_classloader() throws Throwable {
    ClassLoader original = TestRunnersClassLoaderScoped.class.getClassLoader();
    AtomicReference<ClassLoader> scope = new AtomicReference<ClassLoader>();
    Test test = classLoaderScoped(newCase("name", () -> {
      scope.set(Thread.currentThread().getContextClassLoader());
    }));

    runAndThrow(test);

    assertEquals(scope.get().getParent(), original);
  }

  private static void restores_context_class_loader_if_successful() throws Throwable {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    Test test = classLoaderScoped(mockCase("name"));

    runAndThrow(test);

    assertEquals(Thread.currentThread().getContextClassLoader(), original);
  }

  private static void restores_context_class_loader_if_failed() {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    Test test = classLoaderScoped(mockCase("name", new Throwable()));

    try {
      runAndThrow(test);
    } catch (Throwable t) {}

    assertEquals(Thread.currentThread().getContextClassLoader(), original);
  }
}
