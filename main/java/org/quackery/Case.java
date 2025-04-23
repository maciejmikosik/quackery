package org.quackery;

import static org.quackery.QuackeryException.check;

public final class Case implements Test {
  public final String name;
  public final Body body;

  private Case(String name, Body body) {
    this.name = name;
    this.body = body;
  }

  public static Case newCase(String name, Body body) {
    check(name != null);
    check(body != null);
    return new Case(name, body);
  }
}
