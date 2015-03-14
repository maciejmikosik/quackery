package org.testanza;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static org.testanza.Testers.hasConstructor;
import static org.testanza.testing.Assertions.assertEquals;
import static org.testanza.testing.Assertions.assertTrue;
import static org.testanza.testing.Assertions.fail;
import static org.testanza.testing.Tests.run;

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
      fail();
    } catch (TestanzaAssertionException e) {}
  }

  public void fails_if_class_has_constructor_with_different_parameters() throws Throwable {
    tester = hasConstructor(PUBLIC, String.class, String.class);
    test = tester.test(Testable.class);

    try {
      run(test);
      fail();
    } catch (TestanzaAssertionException e) {}
  }

  public void failure_prints_message() throws Throwable {
    tester = hasConstructor(PRIVATE, String.class, String.class);
    test = tester.test(Testable.class);

    try {
      run(test);
      fail();
    } catch (TestanzaAssertionException e) {
      assertTrue(e.getMessage().contains("" //
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
    assertEquals(((Case) test).name,
        "class Testable has private constructor with 2 parameters String, String");
  }

  public void parameters_array_cannot_be_null() {
    try {
      hasConstructor(PRIVATE, (Class[]) null);
      fail();
    } catch (TestanzaException e) {}
  }

  public void parameter_cannot_be_null() {
    try {
      hasConstructor(PRIVATE, String.class, null, String.class);
      fail();
    } catch (TestanzaException e) {}
  }
}
