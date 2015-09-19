package org.quackery;

import static org.quackery.QuackeryException.check;

public abstract class Test {
  public final String name;

  protected Test(String name) {
    check(name != null);
    this.name = name;
  }
}
