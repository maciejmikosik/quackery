package org.quackery.junit;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default.NO_CONSTRUCTORS;
import static org.quackery.Story.story;
import static org.quackery.Suite.suite;
import static org.quackery.junit.JunitClassBuilder.annotationIgnore;
import static org.quackery.junit.JunitClassBuilder.annotationJunitTest;
import static org.quackery.junit.JunitClassBuilder.annotationQuackery;
import static org.quackery.junit.JunitClassBuilder.defaultJunitMethod;
import static org.quackery.junit.JunitClassBuilder.defaultQuackeryMethod;
import static org.quackery.junit.TestingJunit.assertResult;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.mockStory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.quackery.QuackeryException;
import org.quackery.Story;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class TestQuackeryRunner {
  private static final String className = "a.b.AnnotatedClass";

  public static void test_quackery_runner() {
    quackery_story_is_run_once();
    quackery_story_result_is_translated();
    quackery_test_name_is_simplified();
    junit_tests_are_included();
    junit_ignore_annotation_is_handled();
    annotated_methods_are_combined_into_one_tree();
    class_definition_is_validated();
  }

  private static void quackery_story_is_run_once() {
    AtomicInteger invoked = new AtomicInteger();
    new JUnitCore()
        .run(new JunitClassBuilder()
            .define(defaultQuackeryMethod()
                .returning(story("story", () -> {
                  invoked.incrementAndGet();
                })))
            .load());
    assertEquals(invoked.get(), 1);
  }

  private static void quackery_story_result_is_translated() {
    assertResult(
        defaultQuackeryMethod()
            .returning(mockStory("story")),
        mockStory("story"));

    assertResult(
        defaultQuackeryMethod()
            .returning(mockStory("story", new Throwable())),
        mockStory("story", new Throwable()));

    assertResult(
        defaultQuackeryMethod()
            .returning(mockStory("story", new AssertException("message"))),
        mockStory("story", new AssertionError("message")));

    assertResult(
        defaultQuackeryMethod()
            .returning(mockStory("story", new AssumeException("message"))),
        mockStory("story"));
  }

  private static void quackery_test_name_is_simplified() {
    assertResult(
        defaultQuackeryMethod()
            .returning(suite("")
                .add(mockStory(""))
                .add(mockStory(""))),
        suite("[empty_name]")
            .add(mockStory("[empty_name]"))
            .add(mockStory("[empty_name]")));

    assertResult(
        defaultQuackeryMethod()
            .returning(suite("suite\nsuite\rsuite")
                .add(mockStory("storyA\nstoryA\rstoryA"))
                .add(mockStory("storyB\nstoryB\rstoryB"))),
        suite("suite suite suite")
            .add(mockStory("storyA storyA storyA"))
            .add(mockStory("storyB storyB storyB")));

    assertResult(
        defaultQuackeryMethod()
            .returning(suite("suite")
                .add(mockStory("story"))
                .add(mockStory("story"))),
        suite("suite")
            .add(mockStory("story"))
            .add(mockStory("story")));

    assertEquals(
        new QuackeryRunner(new JunitClassBuilder()
            .name(className)
            .define(defaultQuackeryMethod()
                .returning(mockStory("story")))
            .load())
                .getDescription()
                .getDisplayName(),
        className);
  }

  private static void junit_tests_are_included() {
    assertResult(
        defaultJunitMethod()
            .name("junit_method"),
        mockStory("junit_method"));

    assertResult(
        defaultJunitMethod()
            .name("junit_method")
            .throwing(IOException.class),
        mockStory("junit_method", new IOException()));
  }

  private static void junit_ignore_annotation_is_handled() {
    assertResult(
        new JunitClassBuilder()
            .name(className)
            .define(defaultQuackeryMethod()
                .annotations(annotationQuackery(), annotationIgnore(""))
                .returning(mockStory("story", new IOException())))
            .define(defaultJunitMethod()
                .name("junit_method")
                .annotations(annotationJunitTest(), annotationIgnore(""))
                .throwing(IOException.class)),
        suite(className)
            .add(mockStory("junit_method"))
            .add(mockStory("story", new IOException())));

    Result result = new JUnitCore()
        .run(new JunitClassBuilder()
            .name(className)
            .annotate(annotationIgnore(""))
            .define(defaultQuackeryMethod()
                .name("quackeryA")
                .returning(mockStory("storyA", new IOException())))
            .define(defaultQuackeryMethod()
                .name("quackeryB")
                .returning(mockStory("storyB", new IOException())))
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
        new JunitClassBuilder()
            .name(className),
        mockStory(className));

    assertResult(
        new JunitClassBuilder()
            .name(className)
            .define(defaultQuackeryMethod()
                .returning(suite("suite"))),
        suite(className)
            .add(mockStory("suite")));

    assertResult(
        new JunitClassBuilder()
            .name(className)
            .define(defaultQuackeryMethod()
                .name("quackeryA")
                .returning(mockStory("storyA")))
            .define(defaultQuackeryMethod()
                .name("quackeryB")
                .returning(mockStory("storyB"))),
        suite(className)
            .add(mockStory("storyA"))
            .add(mockStory("storyB")));

    assertResult(
        new JunitClassBuilder()
            .name(className)
            .define(defaultQuackeryMethod()
                .returning(mockStory("story")))
            .define(defaultJunitMethod()
                .name("junit")),
        suite(className)
            .add(mockStory("junit"))
            .add(mockStory("story")));

    assertResult(
        new JunitClassBuilder()
            .name(className)
            .define(defaultJunitMethod()
                .name("junitA"))
            .define(defaultJunitMethod()
                .name("junitB")),
        suite(className)
            .add(mockStory("junitA"))
            .add(mockStory("junitB")));

    assertResult(
        new JunitClassBuilder()
            .name(className)
            .define(defaultQuackeryMethod()
                .name("quackery_method")
                .throwing(IOException.class)),
        suite(className)
            .add(mockStory("quackery_method", new IOException())));
  }

  private static void class_definition_is_validated() {
    assertResult(
        defaultQuackeryMethod()
            .name("quackery_method")
            .modifiers(STATIC)
            .returning(mockStory("story")),
        mockStory("quackery_method", new QuackeryException("method must be public")));

    assertResult(
        defaultQuackeryMethod()
            .name("quackery_method")
            .modifiers(PUBLIC)
            .returning(mockStory("story")),
        mockStory("quackery_method", new QuackeryException("method must be static")));

    assertResult(
        defaultQuackeryMethod()
            .name("quackery_method")
            .returnType(Object.class)
            .returning(mockStory("story")),
        mockStory("quackery_method", new QuackeryException(
            "method return type must be assignable to " + Test.class.getName())));
    assertResult(
        defaultQuackeryMethod()
            .returnType(Suite.class)
            .returning(suite("suite")
                .add(mockStory("story"))),
        suite("suite")
            .add(mockStory("story")));
    assertResult(
        defaultQuackeryMethod()
            .returnType(Story.class)
            .returning(mockStory("story")),
        mockStory("story"));

    assertResult(
        defaultQuackeryMethod()
            .name("quackery_method")
            .parameters(Object.class)
            .returning(mockStory("story")),
        mockStory("quackery_method", new QuackeryException("method cannot have parameters")));

    assertResult(
        defaultJunitMethod()
            .name("junit_method")
            .modifiers(PRIVATE),
        mockStory("junit_method", new Exception("Method junit_method() should be public")));

    assertResult(
        defaultJunitMethod()
            .name("junit_method")
            .modifiers(PUBLIC | STATIC),
        mockStory("junit_method", new Exception("Method junit_method() should not be static")));

    assertResult(
        defaultJunitMethod()
            .name("junit_method")
            .returnType(Object.class),
        mockStory("junit_method", new Exception("Method junit_method() should be void")));

    assertResult(
        defaultJunitMethod()
            .name("junit_method")
            .parameters(Object.class),
        mockStory("junit_method", new Exception("Method junit_method should have no parameters")));

    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .name(className)
            .define(defaultJunitMethod()),
        suite(className)
            .add(mockStory(
                "Test class should have exactly one public constructor",
                new Exception("Test class should have exactly one public constructor"))));
    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .name(className)
            .define(defaultQuackeryMethod()
                .returning(mockStory("story"))),
        suite(className)
            .add(mockStory("story")));

    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .name(className)
            .defineConstructor(new MethodDefinition()
                .modifiers(PRIVATE))
            .define(defaultJunitMethod()),
        suite(className)
            .add(mockStory(
                "Test class should have exactly one public constructor",
                new Exception("Test class should have exactly one public constructor"))));
    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .name(className)
            .defineConstructor(new MethodDefinition()
                .modifiers(PRIVATE))
            .define(defaultQuackeryMethod()
                .returning(mockStory("story"))),
        suite(className)
            .add(mockStory("story")));

    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .name(className)
            .defineConstructor(new MethodDefinition()
                .modifiers(PUBLIC)
                .parameters(Object.class))
            .define(defaultJunitMethod()),
        suite(className)
            .add(mockStory(
                "Test class should have exactly one public zero-argument constructor",
                new Exception("Test class should have exactly one public zero-argument constructor"))));
    assertResult(
        new JunitClassBuilder(NO_CONSTRUCTORS)
            .name(className)
            .defineConstructor(new MethodDefinition()
                .modifiers(PUBLIC)
                .parameters(Object.class))
            .define(defaultQuackeryMethod()
                .returning(mockStory("story"))),
        suite(className)
            .add(mockStory("story")));
  }
}
