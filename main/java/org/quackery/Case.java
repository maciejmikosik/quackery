package org.quackery;

public abstract class Case extends Test {
  protected Case(String name) {
    super(name);
  }

  public abstract void run() throws Throwable;
}
