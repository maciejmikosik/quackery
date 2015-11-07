package org.quackery.run;

import static org.quackery.Suite.suite;
import static org.quackery.run.Reports.print;
import static org.quackery.testing.Assertions.assertTrue;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockCase;

import org.quackery.QuackeryException;
import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class test_Reports_print {
  private final String name = "name";
  private final String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g", h = "h";
  private Test report;
  private String printed;

  public void prints_name_of_single_case() {
    report = mockCase(name);

    printed = print(report);

    assertTrue(printed.contains(name));
  }

  public void prints_name_of_deep_case() {
    report = suite("suite")
        .add(mockCase(name));

    printed = print(report);

    assertTrue(printed.contains(name));
  }

  public void prints_name_of_suite() {
    report = suite(name);

    printed = print(report);

    assertTrue(printed.contains(name));
  }

  public void failure_is_marked() {
    report = mockCase(name, new AssertException());

    printed = print(report);

    assertTrue(printed.contains("[AssertException] " + name));
  }

  public void error_is_marked() {
    report = mockCase(name, new Throwable());

    printed = print(report);

    assertTrue(printed.contains("[Throwable] " + name));
  }

  public void misassumption_is_marked() {
    report = mockCase(name, new AssumeException());

    printed = print(report);

    assertTrue(printed.contains("[AssumeException] " + name));
  }

  public void success_is_not_marked() {
    report = mockCase(name);

    printed = print(report);

    assertTrue(!printed.contains("["));
    assertTrue(!printed.contains("]"));

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

    printed = print(report);

    assertTrue(printed.matches(""
        + "a\n"
        + "  b\n"
        + "    c\n"
        + "    d\n"
        + "  e\n"
        + "    f\n"
        + "    g\n"
        + "    h\n"
        ));
  }

  public void test_cannot_be_null() {
    try {
      print(null);
      fail();
    } catch (QuackeryException exception) {}
  }
}
