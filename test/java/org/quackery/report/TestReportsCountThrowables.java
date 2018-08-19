package org.quackery.report;

import static org.quackery.Suite.suite;
import static org.quackery.report.Reports.count;
import static org.quackery.testing.Testing.assertEquals;
import static org.quackery.testing.Testing.fail;
import static org.quackery.testing.Testing.mockCase;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class TestReportsCountThrowables {
  public static void test_reports_count_throwables() {
    does_not_count_successes();
    counts_same_type_exception();
    counts_subtyped_exception();
    does_not_count_supertyped_exception();
    sums_all_throwables_in_hierarchy();
    validates_arguments();
  }

  private static void does_not_count_successes() {
    Test test = mockCase("name");

    int count = count(RuntimeException.class, test);

    assertEquals(count, 0);
  }

  private static void counts_same_type_exception() {
    class SomeException extends RuntimeException {}
    Test test = mockCase("name", new SomeException());

    int count = count(SomeException.class, test);

    assertEquals(count, 1);
  }

  private static void counts_subtyped_exception() {
    class SomeException extends RuntimeException {}
    class SubException extends SomeException {}
    Test test = mockCase("name", new SubException());

    int count = count(SomeException.class, test);

    assertEquals(count, 1);
  }

  private static void does_not_count_supertyped_exception() {
    class SuperException extends RuntimeException {}
    class SomeException extends SuperException {}
    Test test = mockCase("name", new SuperException());

    int count = count(SomeException.class, test);

    assertEquals(count, 0);
  }

  private static void sums_all_throwables_in_hierarchy() {
    Test test = suite("name")
        .add(suite("name")
            .add(mockCase("name"))
            .add(mockCase("name", new RuntimeException()))
            .add(mockCase("name", new Exception())))
        .add(suite("name")
            .add(mockCase("name"))
            .add(mockCase("name", new Throwable()))
            .add(mockCase("name", new Error())));

    int count = count(Throwable.class, test);

    assertEquals(count, 4);
  }

  private static void validates_arguments() {
    Test test = mockCase("name");
    try {
      count(Throwable.class, null);
      fail();
    } catch (QuackeryException e) {}
    try {
      count(null, test);
      fail();
    } catch (QuackeryException e) {}
  }
}
