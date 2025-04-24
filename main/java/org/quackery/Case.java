package org.quackery;

import static org.quackery.QuackeryException.check;

public final class Case implements Test {
  public final String name;
  public final Script script;

  private Case(String name, Script script) {
    this.name = name;
    this.script = script;
  }

  public static Case newCase(String name, Script script) {
    check(name != null);
    check(script != null);
    return new Case(name, script);
  }
}
