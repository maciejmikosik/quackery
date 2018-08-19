package org.quackery.junit;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.NO_CONSTRUCTORS;
import static org.quackery.Suite.suite;
import static org.quackery.junit.JunitClassBuilder.annotationIgnore;
import static org.quackery.junit.JunitClassBuilder.annotationJunitTest;
import static org.quackery.junit.JunitClassBuilder.annotationQuackery;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.junit.TestingJunit.assertResult;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.mockCase;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class TestQuackeryRunner {
  public static void test_quackery_runner() {
    quackery_cases_are_run_once();
    quackery_case_results_are_translated();
    quackery_test_names_are_simplified();
    junit_tests_are_included();
    junit_ignore_annotation_is_handled();
    annotated_methods_are_combined_into_one_tree();
    class_definition_is_validated();
  }

  private static void quackery_cases_are_run_once() {
    final AtomicInteger invoked = new AtomicInteger();
    new JUnitCore()
        .run(new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(new Case("case") {
                  public void run() {
                    invoked.incrementAndGet();
                  }
                }))
            .load());
    assertEquals(invoked.get(), 1);
  }

  private static void quackery_case_results_are_translated() {
    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(mockCase("case"))),
        mockCase("case"));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(mockCase("case", new Throwable()))),
        mockCase("case", new Throwable()));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(mockCase("case", new AssertException("message")))),
        mockCase("case", new AssertionError("message")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(mockCase("case", new AssumeException("message")))),
        mockCase("case"));
  }

  private static void quackery_test_names_are_simplified() {
    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(suite("")
                    .add(mockCase(""))
                    .add(mockCase("")))),
        suite("[empty_name]")
            .add(mockCase("[empty_name]"))
            .add(mockCase("[empty_name]")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(suite("suite\nsuite\rsuite")
                    .add(mockCase("caseA\ncaseA\rcaseA"))
                    .add(mockCase("caseB\ncaseB\rcaseB")))),
        suite("suite suite suite")
            .add(mockCase("caseA caseA caseA"))
            .add(mockCase("caseB caseB caseB")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(suite("suite")
                    .add(mockCase("case"))
                    .add(mockCase("case")))),
        suite("suite")
            .add(mockCase("case"))
            .add(mockCase("case")));

    assertEquals(
        new QuackeryRunner(new JunitClassBuilder("a.b.AnnotateClass")
            .define(defaultQuackeryMethod()
                .returning(mockCase("case")))
            .load())
                .getDescription()
                .getDisplayName(),
        "case(a.b.AnnotateClass)");
  }

  private static void junit_tests_are_included() {
    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass")
            .define(defaultJunitMethod()
                .name("junit_method")),
        suite("a.b.AnnotatedClass")
            .add(mockCase("junit_method")));

    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass")
            .define(defaultJunitMethod()
                .name("junit_method")
                .throwing(IOException.class)),
        suite("a.b.AnnotatedClass")
            .add(mockCase("junit_method", new IOException())));
  }

  private static void junit_ignore_annotation_is_handled() {
    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass")
            .define(defaultQuackeryMethod()
                .annotations(annotationQuackery(), annotationIgnore(""))
                .returning(mockCase("case", new IOException())))
            .define(defaultJunitMethod()
                .name("junit_method")
                .annotations(annotationJunitTest(), annotationIgnore(""))
                .throwing(IOException.class)),
        suite("a.b.AnnotatedClass")
            .add(mockCase("junit_method"))
            .add(mockCase("case", new IOException())));

    Result result = new JUnitCore()
        .run(new JunitClassBuilder("a.b.AnnotatedClass")
            .annotate(annotationIgnore(""))
            .define(defaultQuackeryMethod()
                .name("quackeryA")
                .returning(mockCase("caseA", new IOException())))
            .define(defaultQuackeryMethod()
                .name("quackeryB")
                .returning(mockCase("caseB", new IOException())))
            .define(defaultJunitMethod()
                .name("junitA")
                .throwing(IOException.class))
            .define(defaultJunitMethod()
                .name("junitB")
                .throwing(IOException.class))
            .load());
    assertEquals(result.getRunCount(), 0);
    assertEquals(result.getFailureCount(), 0);
    assertEquals(result.getIgnoreCount(), 1);
  }

  private static void annotated_methods_are_combined_into_one_tree() {
    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass"),
        mockCase("a.b.AnnotatedClass"));

    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass")
            .define(defaultQuackeryMethod()
                .returning(suite("suite"))),
        mockCase("suite"));

    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass")
            .define(defaultQuackeryMethod()
                .name("quackeryA")
                .returning(mockCase("caseA")))
            .define(defaultQuackeryMethod()
                .name("quackeryB")
                .returning(mockCase("caseB"))),
        suite("a.b.AnnotatedClass")
            .add(mockCase("caseA"))
            .add(mockCase("caseB")));

    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass")
            .define(defaultQuackeryMethod()
                .returning(mockCase("case")))
            .define(defaultJunitMethod()
                .name("junit")),
        suite("a.b.AnnotatedClass")
            .add(mockCase("junit"))
            .add(mockCase("case")));

    assertResult(
        new JunitClassBuilder("a.b.AnnotatedClass")
            .define(defaultJunitMethod()
                .name("junitA"))
            .define(defaultJunitMethod()
                .name("junitB")),
        suite("a.b.AnnotatedClass")
            .add(mockCase("junitA"))
            .add(mockCase("junitB")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .name("quackery_method")
                .throwing(IOException.class)),
        mockCase("quackery_method", new IOException()));
  }

  private static void class_definition_is_validated() {
    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .name("quackery_method")
                .modifiers(STATIC)
                .returning(mockCase("case"))),
        mockCase("quackery_method", new QuackeryException("method must be public")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .name("quackery_method")
                .modifiers(PUBLIC)
                .returning(mockCase("case"))),
        mockCase("quackery_method", new QuackeryException("method must be static")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .name("quackery_method")
                .returnType(Object.class)
                .returning(mockCase("case"))),
        mockCase("quackery_method", new QuackeryException(
            "method return type must be assignable to " + Test.class.getName())));
    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returnType(Suite.class)
                .returning(suite("suite")
                    .add(mockCase("case")))),
        suite("suite")
            .add(mockCase("case")));
    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returnType(Case.class)
                .returning(mockCase("case"))),
        mockCase("case"));

    assertResult(
        new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .name("quackery_method")
                .parameters(Object.class)
                .returning(mockCase("case"))),
        mockCase("quackery_method", new QuackeryException("method cannot have parameters")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultJunitMethod()
                .name("junit_method")
                .modifiers(PRIVATE)),
        mockCase("junit_method", new Exception("Method junit_method() should be public")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultJunitMethod()
                .name("junit_method")
                .modifiers(PUBLIC | STATIC)),
        mockCase("junit_method", new Exception("Method junit_method() should not be static")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultJunitMethod()
                .name("junit_method")
                .returnType(Object.class)),
        mockCase("junit_method", new Exception("Method junit_method() should be void")));

    assertResult(
        new JunitClassBuilder()
            .define(defaultJunitMethod()
                .name("junit_method")
                .parameters(Object.class)),
        mockCase("junit_method", new Exception("Method junit_method should have no parameters")));

    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .define(defaultJunitMethod()),
        mockCase(
            "Test class should have exactly one public constructor",
            new Exception("Test class should have exactly one public constructor")));
    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .define(defaultQuackeryMethod()
                .returning(mockCase("case"))),
        mockCase("case"));

    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .defineConstructor(new MethodDefinition()
                .modifiers(PRIVATE))
            .define(defaultJunitMethod()),
        mockCase(
            "Test class should have exactly one public constructor",
            new Exception("Test class should have exactly one public constructor")));
    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .defineConstructor(new MethodDefinition()
                .modifiers(PRIVATE))
            .define(defaultQuackeryMethod()
                .returning(mockCase("case"))),
        mockCase("case"));

    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .defineConstructor(new MethodDefinition()
                .modifiers(PUBLIC)
                .parameters(Object.class))
            .define(defaultJunitMethod()),
        mockCase(
            "Test class should have exactly one public zero-argument constructor",
            new Exception("Test class should have exactly one public zero-argument constructor")));
    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .defineConstructor(new MethodDefinition()
                .modifiers(PUBLIC)
                .parameters(Object.class))
            .define(defaultQuackeryMethod()
                .returning(mockCase("case"))),
        mockCase("case"));
  }
}
