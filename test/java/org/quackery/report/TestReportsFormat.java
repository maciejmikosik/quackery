package org.quackery.report;

import static org.quackery.Suite.suite;
import static org.quackery.report.Reports.format;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestReportsFormat {
  public static void test_reports_format() {
    formats_name_of_single_case();
    formats_name_of_deep_case();
    formats_name_of_suite();
    marks_failure();
    marks_error();
    marks_misassumption();
    marks_skips_success();
    indents_to_reflect_hierarchy();
    validates_argument();
  }

  private static void formats_name_of_single_case() {
    Test report = mockCase("case");

    String formatted = format(report);

    assertTrue(formatted.contains("case"));
  }

  private static void formats_name_of_deep_case() {
    Test report = suite("suite")
        .add(mockCase("case"));

    String formatted = format(report);

    assertTrue(formatted.contains("case"));
  }

  private static void formats_name_of_suite() {
    Test report = suite("suite");

    String formatted = format(report);

    assertTrue(formatted.contains("suite"));
  }

  private static void marks_failure() {
    Test report = mockCase("case", new AssertException());

    String formatted = format(report);

    assertTrue(formatted.contains("[AssertException] " + "case"));
  }

  private static void marks_error() {
    Test report = mockCase("case", new Throwable());

    String formatted = format(report);

    assertTrue(formatted.contains("[Throwable] case"));
  }

  private static void marks_misassumption() {
    Test report = mockCase("case", new AssumeException());

    String formatted = format(report);

    assertTrue(formatted.contains("[AssumeException] case"));
  }

  private static void marks_skips_success() {
    Test report = mockCase("case");

    String formatted = format(report);

    assertTrue(!formatted.contains("["));
    assertTrue(!formatted.contains("]"));

  }

  private static void indents_to_reflect_hierarchy() {
    Test report = suite("a")
        .add(suite("b")
            .add(mockCase("c"))
            .add(mockCase("d")))
        .add(suite("e")
            .add(mockCase("f"))
            .add(mockCase("g"))
            .add(mockCase("h")));

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
