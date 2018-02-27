package org.quackery.run;

import static org.quackery.Suite.suite;
import static org.quackery.testing.Assertions.assertEquals;
import static org.quackery.testing.Assertions.fail;
import static org.quackery.testing.Mocks.mockCase;

import org.quackery.Case;
import org.quackery.QuackeryException;
import org.quackery.Suite;
import org.quackery.Test;

public abstract class TestVisitor {
  private final String name = "name", nameA = "nameA", nameB = "nameB", nameC = "nameC",
      nameD = "nameD", nameE = "nameE", nameF = "nameF";
  private Test test, visited;
  private final Throwable throwable = new Throwable();

  protected abstract Test visit(Test visiting);

  public void keeps_successful_case_name() {
    test = mockCase(name);

    // when
    visited = visit(test);

    // then
    assertEquals(visited.name, name);
  }

  public void keeps_failed_case_name() {
    test = mockCase(name, throwable);

    // when
    visited = visit(test);

    // then
    assertEquals(visited.name, name);
  }

  public void keeps_deep_case_name() {
    test = suite(nameA)
        .add(mockCase(name));

    // when
    visited = visit(test);

    // then
    assertEquals(navigate(visited, 0).name, name);
  }

  public void keeps_suite_name() {
    test = suite(name);

    // when
    visited = visit(test);

    // then
    assertEquals(visited.name, name);
  }

  public void imitates_successful_case() throws Throwable {
    test = mockCase(name);
    visited = visit(test);

    // when
    ((Case) visited).run();

    // then no exception
  }

  public void imitates_failed_case() throws Throwable {
    test = mockCase(name, throwable);
    visited = visit(test);

    try {
      // when
      ((Case) visited).run();
      fail();
      // then
    } catch (Throwable t) {
      assertEquals(t, throwable);
    }
  }

  public void copies_suite_hierarchy() {
    test = suite(name)
        .add(suite(nameA)
            .add(mockCase(nameC))
            .add(mockCase(nameD)))
        .add(suite(nameB)
            .add(mockCase(nameE))
            .add(mockCase(nameF)));

    // when
    visited = visit(test);

    // then
    assertEquals(visited.name, name);
    assertEquals(navigate(Suite.class, visited).tests.size(), 2);
    assertEquals(navigate(visited, 0).name, nameA);
    assertEquals(navigate(visited, 1).name, nameB);

    assertEquals(navigate(Suite.class, visited, 0).tests.size(), 2);
    assertEquals(navigate(visited, 0, 0).name, nameC);
    assertEquals(navigate(visited, 0, 1).name, nameD);

    assertEquals(navigate(Suite.class, visited, 1).tests.size(), 2);
    assertEquals(navigate(visited, 1, 0).name, nameE);
    assertEquals(navigate(visited, 1, 1).name, nameF);
  }

  public void cannot_visit_null() {
    try {
      visit(null);
      fail();
    } catch (QuackeryException e) {}
  }

  private static Test navigate(Test test, int... path) {
    Test result = test;
    for (int index : path) {
      result = ((Suite) result).tests.get(index);
    }
    return result;
  }

  private static <T extends Test> T navigate(Class<T> cast, Test test, int... path) {
    return (T) navigate(test, path);
  }
}
