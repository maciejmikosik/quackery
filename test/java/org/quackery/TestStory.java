package org.quackery;

import static org.junit.Assert.fail;
import static org.quackery.Story.story;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.nameOf;
import static org.quackery.testing.Testing.runAndThrow;

import java.util.concurrent.atomic.AtomicInteger;

public class TestStory {
  public static void test_story() throws Throwable {
    implements_test_interface();
    assigns_name();
    script_is_run_once();
    script_is_run_each_time();
    script_can_throw_exception();
    validates_arguments();
  }

  private static void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Story.class));
  }

  private static void assigns_name() {
    String name = "name";

    Test test = story(name, () -> {});

    assertEquals(nameOf(test), name);
  }

  private static void script_is_run_once() throws Throwable {
    AtomicInteger invoked = new AtomicInteger();
    Test test = story("name", () -> {
      invoked.incrementAndGet();
    });

    runAndThrow(test);

    assertEquals(invoked.get(), 1);
  }

  private static void script_is_run_each_time() throws Throwable {
    AtomicInteger invoked = new AtomicInteger();
    Test test = story("name", () -> {
      invoked.incrementAndGet();
    });

    runAndThrow(test);
    runAndThrow(test);
    runAndThrow(test);

    assertEquals(invoked.get(), 3);
  }

  private static void script_can_throw_exception() {
    Throwable throwable = new Throwable();
    Test test = story("name", () -> {
      throw throwable;
    });

    try {
      runAndThrow(test);
      fail();
    } catch (Throwable e) {
      assertEquals(e, throwable);
    }
  }

  private static void validates_arguments() {
    try {
      story(null, () -> {});
      fail();
    } catch (QuackeryException e) {}
    try {
      story("name", null);
      fail();
    } catch (QuackeryException e) {}
  }
}
