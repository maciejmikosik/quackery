package org.testanza;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static org.testanza.Testers.hasConstructor;
import static org.testanza.Testilities.run;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;

import java.lang.reflect.Modifier;

public class describe_Testers_hasConstructor {
  private Tester<Class<?>> tester;
  private Test test;

  private static class Testable {
    @SuppressWarnings("unused")
    public Testable(String s) {}
  }

  public void succeeds_if_class_has_constructor() throws Throwable {
    tester = hasConstructor(PUBLIC, String.class);
    test = tester.test(Testable.class);

    run(test);
  }

  public void fails_if_class_has_constructor_with_different_access() throws Throwable {
    tester = hasConstructor(PRIVATE, String.class);
    test = tester.test(Testable.class);

    try {
      run(test);
      verifyFail();
    } catch (TestanzaAssertionError e) {}
  }

  public void fails_if_class_has_constructor_with_different_parameters() throws Throwable {
    tester = hasConstructor(PUBLIC, String.class, String.class);
    test = tester.test(Testable.class);

    try {
      run(test);
      verifyFail();
    } catch (TestanzaAssertionError e) {}
  }

  public void failure_prints_message() throws Throwable {
    tester = hasConstructor(PRIVATE, String.class, String.class);
    test = tester.test(Testable.class);

    try {
      run(test);
      verifyFail();
    } catch (TestanzaAssertionError e) {
      verify(e.getMessage().contains("" //
          + "\n" //
          + "  expected that\n" //
          + "    " + Testable.class + "\n" //
          + "  has constructor with modifier\n" //
          + "    " + Modifier.toString(PRIVATE) + "\n" //
          + "  and 2 parameters\n" //
          + "    " + String.class + "\n" //
          + "    " + String.class + "\n" //
      ));
    }
  }

  public void name_contains_type_modifier_and_parameters() {
    tester = hasConstructor(PRIVATE, String.class, String.class);
    test = tester.test(Testable.class);
    verifyEquals(((Case) test).name,
        "class Testable has private constructor with 2 parameters String, String");
  }

  public void parameters_array_cannot_be_null() {
    try {
      hasConstructor(PRIVATE, (Class[]) null);
      verifyFail();
    } catch (TestanzaException e) {}
  }

  public void parameter_cannot_be_null() {
    try {
      hasConstructor(PRIVATE, String.class, null, String.class);
      verifyFail();
    } catch (TestanzaException e) {}
  }
}
