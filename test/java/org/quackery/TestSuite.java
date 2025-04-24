package org.quackery;

import static java.util.Arrays.asList;
import static org.quackery.Suite.suite;
import static org.quackery.testing.Testing.assertChildren;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockStory;
import static org.quackery.testing.Testing.nameOf;

import java.util.List;

public class TestSuite {
  public static void test_suite() {
    implements_test_interface();
    creates_empty_suite();
    adds_test();
    adds_tests_from_iterable();
    adds_tests_from_array();
    allows_wildcards();
    implements_to_string();
    validates_arguments();
  }

  private static void implements_test_interface() {
    assertTrue(Test.class.isAssignableFrom(Suite.class));
  }

  private static void creates_empty_suite() {
    String name = "name";
    Suite suite = suite(name);

    assertEquals(nameOf(suite), name);
    assertChildren(suite, asList());
  }

  private static void adds_test() {
    Test testA = mockStory("storyA");
    Test testB = mockStory("storyB");
    Test testC = mockStory("storyC");

    Suite suite = suite("suite")
        .add(testA)
        .add(testB)
        .add(testC);

    assertChildren(suite, asList(testA, testB, testC));
  }

  private static void adds_tests_from_iterable() {
    Test testA = mockStory("storyA");
    Test testB = mockStory("storyB");
    Test testC = mockStory("storyC");
    Test testD = mockStory("storyD");

    Suite suite = suite("suite")
        .addAll(asList(testA, testB))
        .addAll(asList(testC, testD));

    assertChildren(suite, asList(testA, testB, testC, testD));
  }

  private static void adds_tests_from_array() {
    Test testA = mockStory("storyA");
    Test testB = mockStory("storyB");
    Test testC = mockStory("storyC");
    Test testD = mockStory("storyD");

    Suite suite = suite("suite")
        .addAll(new Test[] { testA, testB })
        .addAll(new Test[] { testC, testD });

    assertChildren(suite, asList(testA, testB, testC, testD));
  }

  private static void allows_wildcards() {
    List<Story> stories = asList((Story) mockStory("story"));
    List<Suite> suites = asList(suite("suite"));
    List<Test> tests = asList(suite("suite"), mockStory("story"));
    List<? extends Test> covariantTests = asList(suite("suite"), mockStory("story"));

    suite("suite")
        .addAll(stories)
        .addAll(suites)
        .addAll(tests)
        .addAll(covariantTests);
  }

  private static void implements_to_string() {
    String name = "name";

    Suite suite = suite(name);

    assertEquals(suite.toString(), name);
  }

  private static void validates_arguments() {
    Suite suite = suite("suite");
    Test testA = mockStory("storyA");
    Test testB = mockStory("storyB");

    try {
      suite(null);
      fail();
    } catch (QuackeryException e) {}

    try {
      suite.add(null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll((Iterable<Test>) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll(asList(testA, null, testB));
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll((Test[]) null);
      fail();
    } catch (QuackeryException e) {}
    try {
      suite.addAll(new Test[] { testA, null, testB });
      fail();
    } catch (QuackeryException e) {}
  }
}
