package org.quackery.run;

import static org.quackery.Suite.suite;
import static org.quackery.run.Reports.count;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockCase;

import org.quackery.QuackeryException;
import org.quackery.Test;

public class test_Reports_count_throwables {
  private Test test;
  private int count;
  private final String name = "name";

  public void does_not_count_successes() {
    test = mockCase(name);

    // when
    count = count(RuntimeException.class, test);

    // then
    assertEquals(count, 0);
  }

  public void counts_same_type_exception() {
    class SomeException extends RuntimeException {}
    test = mockCase(name, new SomeException());

    // when
    count = count(SomeException.class, test);

    // then
    assertEquals(count, 1);
  }

  public void counts_subtyped_exception() {
    class SomeException extends RuntimeException {}
    class SubException extends SomeException {}
    test = mockCase(name, new SubException());

    // when
    count = count(SomeException.class, test);

    // then
    assertEquals(count, 1);
  }

  public void does_not_count_supertyped_exception() {
    class SuperException extends RuntimeException {}
    class SomeException extends SuperException {}
    test = mockCase(name, new SuperException());

    // when
    count = count(SomeException.class, test);

    // then
    assertEquals(count, 0);
  }

  public void sums_all_throwables_in_hierarchy() {
    test = suite(name)
        .test(suite(name)
            .test(mockCase(name))
            .test(mockCase(name, new RuntimeException()))
            .test(mockCase(name, new Exception())))
        .test(suite(name)
            .test(mockCase(name))
            .test(mockCase(name, new Throwable()))
            .test(mockCase(name, new Error())));

    // when
    count = count(Throwable.class, test);

    // then
    assertEquals(count, 4);
  }

  public void test_cannot_be_null() {
    try {
      count(Throwable.class, null);
      fail();
    } catch (QuackeryException e) {}
  }

  public void throwable_type_cannot_be_null() {
    test = mockCase(name);

    try {
      count(null, test);
      fail();
    } catch (QuackeryException e) {}
  }
}
