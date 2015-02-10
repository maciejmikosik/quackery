package org.testanza;

import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;

public class describe_Case {
  private final String name = "name";
  private Case test;

  public void implements_test_interface() {
    verify(Test.class.isAssignableFrom(Case.class));
  }

  public void assigns_name() {
    test = new Case(name) {
      public void run() {}
    };
    verifyEquals(test.name, name);
  }

  public void name_cannot_be_null() {
    try {
      new Case(null) {
        public void run() {}
      };
      verifyFail();
    } catch (TestanzaException e) {}
  }
}
