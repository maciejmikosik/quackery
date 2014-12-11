package org.testanza.testers;

import static org.testanza.Testilities.name;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;
import static org.testanza.testers.TestersForClasses.hasModifier;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;

import org.testanza.Case;
import org.testanza.Test;
import org.testanza.TestanzaAssertionError;

public class describe_TestersForClasses_hasModifier {
  private AnnotatedElement item;
  private Test test;

  public void succeeds_if_method_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      final void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);

    ((Case) test).body.invoke();
  }

  public void fails_if_method_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);

    try {
      ((Case) test).body.invoke();
      verifyFail();
    } catch (TestanzaAssertionError e) {}
  }

  public void succeeds_if_class_has_modifier() throws Throwable {
    @SuppressWarnings("unused")
    final class TestClass {
      final void testMethod() {}
    }
    test = hasModifier(Modifier.FINAL).test(TestClass.class);

    ((Case) test).body.invoke();
  }

  public void fails_if_class_has_no_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    test = hasModifier(Modifier.FINAL).test(TestClass.class);

    try {
      ((Case) test).body.invoke();
      verifyFail();
    } catch (TestanzaAssertionError e) {}
  }

  public void failure_prints_message() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void foo() {}
    }
    item = TestClass.class.getDeclaredMethod("foo");
    test = hasModifier(Modifier.FINAL).test(item);

    try {
      ((Case) test).body.invoke();
      verifyFail();
    } catch (TestanzaAssertionError e) {
      verifyEquals(e.getMessage(), "" //
          + "\n" //
          + "  expected that\n" //
          + "    method " + item.toString() + "\n" //
          + "  has modifier\n" //
          + "    final\n" //
      );
    }
  }

  public void test_name_contains_modifier() throws Throwable {
    @SuppressWarnings("unused")
    class TestClass {
      void testMethod() {}
    }
    item = TestClass.class.getDeclaredMethod("testMethod");
    test = hasModifier(Modifier.FINAL).test(item);

    verify(name(test).contains("final"));
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
    verify(name(test).contains("method foo"));

    item = TestClass.class.getDeclaredField("foo");
    test = hasModifier(Modifier.FINAL).test(item);
    verify(name(test).contains("field foo"));

    item = TestClass.class.getDeclaredConstructors()[0];
    test = hasModifier(Modifier.FINAL).test(item);
    verify(name(test).contains("constructor TestClass"));

    item = TestClass.class;
    test = hasModifier(Modifier.FINAL).test(item);
    verify(name(test).contains("class TestClass"));
  }
}