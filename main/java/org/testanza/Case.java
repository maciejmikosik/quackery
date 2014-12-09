package org.testanza;

import static org.testanza.TestanzaException.check;

public class Case implements Test {
  public final String name;
  public final Closure body;

  private Case(String name, Closure body) {
    this.name = name;
    this.body = body;
  }

  public static Case newCase(String name, Closure body) {
    check(name != null);
    check(body != null);
    return new Case(name, body);
  }
}
