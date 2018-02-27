package org.quackery.report;

import static org.quackery.Suite.suite;
import static org.quackery.report.Reports.format;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockCase;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestReportsFormat {
  private final String name = "name";
  private final String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g", h = "h";
  private Test report;
  private String formatted;

  public void formats_name_of_single_case() {
    report = mockCase(name);

    formatted = format(report);

    assertTrue(formatted.contains(name));
  }

  public void formats_name_of_deep_case() {
    report = suite("suite")
        .add(mockCase(name));

    formatted = format(report);

    assertTrue(formatted.contains(name));
  }

  public void formats_name_of_suite() {
    report = suite(name);

    formatted = format(report);

    assertTrue(formatted.contains(name));
  }

  public void failure_is_marked() {
    report = mockCase(name, new AssertException());

    formatted = format(report);

    assertTrue(formatted.contains("[AssertException] " + name));
  }

  public void error_is_marked() {
    report = mockCase(name, new Throwable());

    formatted = format(report);

    assertTrue(formatted.contains("[Throwable] " + name));
  }

  public void misassumption_is_marked() {
    report = mockCase(name, new AssumeException());

    formatted = format(report);

    assertTrue(formatted.contains("[AssumeException] " + name));
  }

  public void success_is_not_marked() {
    report = mockCase(name);

    formatted = format(report);

    assertTrue(!formatted.contains("["));
    assertTrue(!formatted.contains("]"));

  }

  public void indents_to_reflect_hierarchy() {
    report = suite(a)
        .add(suite(b)
            .add(mockCase(c))
            .add(mockCase(d)))
        .add(suite(e)
            .add(mockCase(f))
            .add(mockCase(g))
            .add(mockCase(h)));

    formatted = format(report);

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

  public void test_cannot_be_null() {
    try {
      format(null);
      fail();
    } catch (QuackeryException exception) {}
  }
}
