package org.quackery;

import static org.quackery.QuackeryException.check;

public abstract class Case implements Test {
  public final String name;

  protected Case(String name) {
    check(name != null);
    this.name = name;
  }

  public abstract void run() throws Throwable;
}
