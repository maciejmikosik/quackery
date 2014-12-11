package org.testanza;

import static org.testanza.Testilities.newObject;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;

public class describe_CaseTester {
  private final String string = "string";
  private CaseTester<Object> tester;
  private final Object object = newObject("item");
  private boolean invoked;
  private final Exception exception = new RuntimeException();

  public void creates_name_for_item() {
    tester = new CaseTester<Object>() {
      protected String name(Object item) {
        return string + item;
      }

      protected void body(Object item) throws Throwable {}
    };
    verifyEquals(tester.name(object), string + object);
  }

  public void invokes_body() throws Throwable {
    tester = new CaseTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void body(Object item) throws Throwable {
        verifyEquals(object, item);
        invoked = true;
      }
    };
    ((Case) tester.test(object)).body.invoke();
    verify(invoked);
  }

  public void throws_from_body() throws Throwable {
    tester = new CaseTester<Object>() {
      protected String name(Object item) {
        return "";
      }

      protected void body(Object item) throws Throwable {
        throw exception;
      }
    };
    try {
      ((Case) tester.test(object)).body.invoke();
      verifyFail();
    } catch (Exception e) {
      verifyEquals(e, exception);
    }
  }
}
