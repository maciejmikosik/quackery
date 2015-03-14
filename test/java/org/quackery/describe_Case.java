package org.quackery;

import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Assertions.fail;

import org.quackery.Case;
import org.quackery.Test;
import org.quackery.QuackeryException;

public class describe_Case {
  private final String name = "name";
  private Case test;

  public void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Case.class));
  }

  public void assigns_name() {
    test = new Case(name) {
      public void run() {}
    };
    assertEquals(test.name, name);
  }

  public void name_cannot_be_null() {
    try {
      new Case(null) {
        public void run() {}
      };
      fail();
    } catch (QuackeryException e) {}
  }
}
