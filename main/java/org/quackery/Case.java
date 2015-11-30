package org.quackery;

import static org.quackery.QuackeryException.check;

public abstract class Case extends Test {
  protected Case(String name) {
    super(name);
  }

  public abstract void run() throws Throwable;

  public interface Body {
    void run() throws Throwable;
  }

  public static Case newCase(String name, final Body body) {
    check(body != null);
    return new Case(name) {
      public void run() throws Throwable {
        body.run();
      }
    };
  }
}
