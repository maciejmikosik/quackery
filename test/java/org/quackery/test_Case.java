package org.quackery;

import static org.junit.Assert.fail;
import static org.quackery.Case.newCase;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertTrue;

import org.quackery.Case.Body;

public class test_Case {
  private final String name = "name";
  private Case test;
  private final Body body = new Body() {
    public void run() throws Throwable {}
  };
  private boolean invoked;
  private final Throwable throwable = new Throwable();

  public void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Case.class));
  }

  public void constructor_assigns_name() {
    test = new Case(name) {
      public void run() {}
    };
    assertEquals(test.name, name);
  }

  public void factory_assigns_name() {
    test = newCase(name, body);
    assertEquals(test.name, name);
  }

  public void factory_body_is_run() throws Throwable {
    test = newCase(name, new Body() {
      public void run() {
        invoked = true;
      }
    });
    test.run();
    assertTrue(invoked);
  }

  public void factory_body_can_throw_exception() throws Throwable {
    test = newCase(name, new Body() {
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

  public void constructor_name_cannot_be_null() {
    try {
      new Case(null) {
        public void run() {}
      };
      fail();
    } catch (QuackeryException e) {}
  }

  public void factory_name_cannot_be_null() {
    try {
      newCase(null, body);
      fail();
    } catch (QuackeryException e) {}
  }

  public void factory_body_cannot_be_null() {
    try {
      newCase(name, null);
      fail();
    } catch (QuackeryException e) {}
  }
}
