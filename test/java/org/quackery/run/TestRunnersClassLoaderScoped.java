package org.quackery.run;

import static org.quackery.run.Runners.classLoaderScoped;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Mocks.mockCase;

import org.quackery.Case;
import org.quackery.Test;

public class TestRunnersClassLoaderScoped extends TestVisitor {
  private final String name = "name";
  private final Throwable throwable = new Throwable();
  private ClassLoader original, scoped, parent;
  private Test test;

  protected Test visit(Test visiting) {
    return classLoaderScoped(visiting);
  }

  public void scoped_is_not_context() throws Throwable {
    original = Thread.currentThread().getContextClassLoader();
    test = classLoaderScoped(new Case(name) {
      public void run() {
        scoped = Thread.currentThread().getContextClassLoader();
      }
    });

    // when
    ((Case) test).run();

    // then
    assertTrue(scoped != original);
  }

  public void scoped_is_not_current() throws Throwable {
    original = getClass().getClassLoader();
    test = classLoaderScoped(new Case(name) {
      public void run() {
        scoped = Thread.currentThread().getContextClassLoader();
      }
    });

    // when
    ((Case) test).run();

    // then
    assertTrue(scoped != original);
  }

  public void scoped_is_child_of_context() throws Throwable {
    original = getClass().getClassLoader();
    parent = new ClassLoader() {};
    Thread.currentThread().setContextClassLoader(parent);
    test = classLoaderScoped(new Case(name) {
      public void run() {
        scoped = Thread.currentThread().getContextClassLoader();
      }
    });

    // when
    ((Case) test).run();

    // then
    assertTrue(scoped.getParent() == parent);

    // tear down
    Thread.currentThread().setContextClassLoader(original);
  }

  public void restores_context_if_successful() throws Throwable {
    original = Thread.currentThread().getContextClassLoader();
    test = classLoaderScoped(mockCase(name));

    // when
    ((Case) test).run();

    // then
    assertEquals(Thread.currentThread().getContextClassLoader(), original);
  }

  public void restores_context_if_failed() throws Throwable {
    original = Thread.currentThread().getContextClassLoader();
    test = classLoaderScoped(mockCase(name, throwable));

    // when
    try {
      ((Case) test).run();
    } catch (Throwable t) {}

    // then
    assertEquals(Thread.currentThread().getContextClassLoader(), original);
  }
}
