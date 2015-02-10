package org.testanza;

import static org.testanza.Testers.isAssignableTo;
import static org.testanza.Testilities.name;
import static org.testanza.Testilities.run;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyFail;

public class describe_Testers_isAssignableTo_Class {
  private Test test;

  public void succeeds_if_type_is_same_type() throws Throwable {
    test = isAssignableTo(Type.class).test(Type.class);
    run(test);
  }

  public void succeeds_if_type_is_subtype() throws Throwable {
    test = isAssignableTo(Type.class).test(SubType.class);
    run(test);
  }

  public void fails_if_type_is_supertype() throws Throwable {
    test = isAssignableTo(Type.class).test(SuperType.class);
    try {
      run(test);
      verifyFail();
    } catch (TestanzaAssertionError e) {}
  }

  public void name_contains_simple_names_of_types() {
    test = isAssignableTo(Type.class).test(SubType.class);
    verify(name(test).contains(Type.class.getSimpleName()));
    verify(name(test).contains(SubType.class.getSimpleName()));
  }

  public void failure_prints_full_names_of_types() throws Throwable {
    test = isAssignableTo(Type.class).test(SuperType.class);
    try {
      run(test);
      verifyFail();
    } catch (TestanzaAssertionError e) {
      verify(e.getMessage().contains(Type.class.getName()));
      verify(e.getMessage().contains(SuperType.class.getSimpleName()));
    }
  }

  private static class SuperType {}

  private static class Type extends SuperType {}

  private static class SubType extends Type {}
}
