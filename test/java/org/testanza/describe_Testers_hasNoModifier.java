package org.testanza;

import static org.testanza.Testers.hasNoModifier;
import static org.testanza.Testilities.name;
import static org.testanza.Testilities.run;
import static org.testanza.testing.Assertions.assertEquals;
import static org.testanza.testing.Assertions.assertTrue;
import static org.testanza.testing.Assertions.fail;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;

public class describe_Testers_hasNoModifier {
  private AnnotatedElement item;
  private Test test;

  public void succeeds_if_method_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasNoModifier(Modifier.FINAL).test(item);

    run(test);
  }

  public void fails_if_method_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      final void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasNoModifier(Modifier.FINAL).test(item);

    try {
      run(test);
      fail();
    } catch (TestanzaAssertionError e) {}
  }

  public void succeeds_if_class_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      final void testMethod() {}
    }
    test = hasNoModifier(Modifier.FINAL).test(TestClass.class);

    run(test);
  }

  public void fails_if_class_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    final class TestClass {
      void testMethod() {}
    }
    test = hasNoModifier(Modifier.FINAL).test(TestClass.class);

    try {
      run(test);
      fail();
    } catch (TestanzaAssertionError e) {}
  }

  public void failure_prints_message() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      final void foo() {}
    }
    item = TestClass.class.getDeclaredMethod("foo");
    test = hasNoModifier(Modifier.FINAL).test(item);

    try {
      run(test);
      fail();
    } catch (TestanzaAssertionError e) {
      assertEquals(e.getMessage(), "" //
          + "\n" //
          + "  expected that\n" //
          + "    method " + item.toString() + "\n" //
          + "  has no modifier\n" //
          + "    final\n");
    }
  }

  public void test_name_contains_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasNoModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("final"));
  }

  public void test_name_contains_member_type_and_simple_name() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      TestClass() {}

      Object foo;

      void foo() {}
    }
    item = TestClass.class.getDeclaredMethod("foo");
    test = hasNoModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("method foo"));

    item = TestClass.class.getDeclaredField("foo");
    test = hasNoModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("field foo"));

    item = TestClass.class.getDeclaredConstructors()[0];
    test = hasNoModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("constructor TestClass"));

    item = TestClass.class;
    test = hasNoModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("class TestClass"));
  }
}
