package org.quackery;

import static org.quackery.Testers.hasModifier;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Tests.name;
import static org.quackery.testing.Tests.run;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;

public class describe_Testers_hasModifier {
  private AnnotatedElement item;
  private Test test;

  public void succeeds_if_method_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      final void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);

    run(test);
  }

  public void fails_if_method_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);

    try {
      run(test);
      fail();
    } catch (FailureException e) {}
  }

  public void succeeds_if_class_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    final class TestClass {
      final void testMethod() {}
    }
    test = hasModifier(Modifier.FINAL).test(TestClass.class);

    run(test);
  }

  public void fails_if_class_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    test = hasModifier(Modifier.FINAL).test(TestClass.class);

    try {
      run(test);
      fail();
    } catch (FailureException e) {}
  }

  public void failure_prints_message() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void foo() {}
    }
    item = TestClass.class.getDeclaredMethod("foo");
    test = hasModifier(Modifier.FINAL).test(item);

    try {
      run(test);
      fail();
    } catch (FailureException e) {
      assertEquals(e.getMessage(), "" //
          + "\n" //
          + "  expected that\n" //
          + "    method " + item.toString() + "\n" //
          + "  has modifier\n" //
          + "    final\n");
    }
  }

  public void test_name_contains_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);

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
    test = hasModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("method foo"));

    item = TestClass.class.getDeclaredField("foo");
    test = hasModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("field foo"));

    item = TestClass.class.getDeclaredConstructors()[0];
    test = hasModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("constructor TestClass"));

    item = TestClass.class;
    test = hasModifier(Modifier.FINAL).test(item);
    assertTrue(name(test).contains("class TestClass"));
  }
}
