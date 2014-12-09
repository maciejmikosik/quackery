package org.testanza;

import static org.testanza.Case.newCase;
import static org.testanza.Testilities.newClosure;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;

public class describe_Case {
  private String name = "name";
  private Closure body = newClosure("body");
  private Case test;

  public void implements_test_interface() {
    verify(Test.class.isAssignableFrom(Case.class));
  }

  public void assigns_fields() {
    test = newCase(name, body);
    verifyEquals(test.name, name);
    verifyEquals(test.body, body);
  }

  public void name_cannot_be_null() {
    name = null;
    try {
      newCase(name, body);
      verifyFail();
    } catch (TestanzaException e) {}
  }

  public void body_cannot_be_null() {
    body = null;
    try {
      newCase(name, body);
      verifyFail();
    } catch (TestanzaException e) {}
  }
}
