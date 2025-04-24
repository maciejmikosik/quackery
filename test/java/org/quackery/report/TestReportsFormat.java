package org.quackery.report;

import static org.quackery.Suite.suite;
import static org.quackery.report.Reports.format;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockStory;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestReportsFormat {
  public static void test_reports_format() {
    formats_name_of_single_story();
    formats_name_of_story_in_suite();
    formats_name_of_suite();
    marks_failure();
    marks_error();
    marks_misassumption();
    marks_skips_success();
    indents_to_reflect_hierarchy();
    validates_argument();
  }

  private static void formats_name_of_single_story() {
    Test report = mockStory("story");

    String formatted = format(report);

    assertTrue(formatted.contains("story"));
  }

  private static void formats_name_of_story_in_suite() {
    Test report = suite("suite")
        .add(mockStory("story"));

    String formatted = format(report);

    assertTrue(formatted.contains("story"));
  }

  private static void formats_name_of_suite() {
    Test report = suite("suite");

    String formatted = format(report);

    assertTrue(formatted.contains("suite"));
  }

  private static void marks_failure() {
    Test report = mockStory("story", new AssertException());

    String formatted = format(report);

    assertTrue(formatted.contains("[AssertException] " + "story"));
  }

  private static void marks_error() {
    Test report = mockStory("story", new Throwable());

    String formatted = format(report);

    assertTrue(formatted.contains("[Throwable] story"));
  }

  private static void marks_misassumption() {
    Test report = mockStory("story", new AssumeException());

    String formatted = format(report);

    assertTrue(formatted.contains("[AssumeException] story"));
  }

  private static void marks_skips_success() {
    Test report = mockStory("story");

    String formatted = format(report);

    assertTrue(!formatted.contains("["));
    assertTrue(!formatted.contains("]"));

  }

  private static void indents_to_reflect_hierarchy() {
    Test report = suite("a")
        .add(suite("b")
            .add(mockStory("c"))
            .add(mockStory("d")))
        .add(suite("e")
            .add(mockStory("f"))
            .add(mockStory("g"))
            .add(mockStory("h")));

    String formatted = format(report);

    assertTrue(formatted.matches(""
        + "a\n"
        + "  b\n"
        + "    c\n"
        + "    d\n"
        + "  e\n"
        + "    f\n"
        + "    g\n"
        + "    h\n"));
  }

  private static void validates_argument() {
    try {
      format(null);
      fail();
    } catch (QuackeryException exception) {}
  }
}
