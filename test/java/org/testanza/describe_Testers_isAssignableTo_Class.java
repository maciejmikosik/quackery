package org.testanza;

import static org.testanza.Testers.isAssignableTo;
import static org.testanza.testing.Assertions.assertTrue;
import static org.testanza.testing.Assertions.fail;
import static org.testanza.testing.Tests.name;
import static org.testanza.testing.Tests.run;

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
      fail();
    } catch (TestanzaAssertionException e) {}
  }

  public void name_contains_simple_names_of_types() {
    test = isAssignableTo(Type.class).test(SubType.class);
    assertTrue(name(test).contains(Type.class.getSimpleName()));
    assertTrue(name(test).contains(SubType.class.getSimpleName()));
  }

  public void failure_prints_full_names_of_types() throws Throwable {
    test = isAssignableTo(Type.class).test(SuperType.class);
    try {
      run(test);
      fail();
    } catch (TestanzaAssertionException e) {
      assertTrue(e.getMessage().contains(Type.class.getName()));
      assertTrue(e.getMessage().contains(SuperType.class.getSimpleName()));
    }
  }

  private static class SuperType {}

  private static class Type extends SuperType {}

  private static class SubType extends Type {}
}
