package org.quackery;

import static org.junit.Assert.fail;
import static org.quackery.Case.newCase;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

public class TestCase {
  public static void test_case() throws Throwable {
    implements_test_interface();
    constructor_assigns_name();
    factory_assigns_name();
    factory_body_is_run_once();
    factory_body_is_run_each_time();
    factory_body_can_throw_exception();
    validates_arguments();
  }

  private static void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Case.class));
  }

  private static void constructor_assigns_name() {
    String name = "name";

    Case test = new Case(name) {
      public void run() {}
    };

    assertEquals(test.name, name);
  }

  private static void factory_assigns_name() {
    String name = "name";

    Case test = newCase(name, new Body() {
      public void run() {}
    });

    assertEquals(test.name, name);
  }

  private static void factory_body_is_run_once() throws Throwable {
    final AtomicInteger invoked = new AtomicInteger();
    Case test = newCase("name", new Body() {
      public void run() {
        invoked.incrementAndGet();
      }
    });

    test.run();

    assertEquals(invoked.get(), 1);
  }

  private static void factory_body_is_run_each_time() throws Throwable {
    final AtomicInteger invoked = new AtomicInteger();
    Case test = newCase("name", new Body() {
      public void run() {
        invoked.incrementAndGet();
      }
    });

    test.run();
    test.run();
    test.run();

    assertEquals(invoked.get(), 3);
  }

  private static void factory_body_can_throw_exception() {
    final Throwable throwable = new Throwable();
    Case test = newCase("name", new Body() {
      public void run() throws Throwable {
        throw throwable;
      }
    });

    try {
      test.run();
      fail();
    } catch (Throwable e) {
      assertEquals(e, throwable);
    }
  }

  private static void validates_arguments() {
    try {
      new Case(null) {
        public void run() {}
      };
      fail();
    } catch (QuackeryException e) {}
    try {
      newCase(null, new Body() {
        public void run() {}
      });
      fail();
    } catch (QuackeryException e) {}
    try {
      newCase("name", null);
      fail();
    } catch (QuackeryException e) {}
  }
}
