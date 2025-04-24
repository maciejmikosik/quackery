package org.quackery;

import static org.quackery.QuackeryException.check;

public final class Story implements Test {
  public final String name;
  public final Script script;

  private Story(String name, Script script) {
    this.name = name;
    this.script = script;
  }

  public static Story story(String name, Script script) {
    check(name != null);
    check(script != null);
    return new Story(name, script);
  }
}
