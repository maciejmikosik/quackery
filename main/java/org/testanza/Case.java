package org.testanza;

import static org.testanza.TestanzaException.check;

public abstract class Case implements Test {
  public final String name;

  protected Case(String name) {
    check(name != null);
    this.name = name;
  }

  public abstract void run() throws Throwable;
}
