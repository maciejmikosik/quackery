package org.quackery.contract.collection;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.fail;
import static org.quackery.AssumptionException.assume;
import static org.quackery.Suite.suite;
import static org.quackery.contract.Commons.assumeConstructor;
import static org.quackery.contract.Commons.copy;
import static org.quackery.contract.Commons.newArrayList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quackery.Case;
import org.quackery.Test;

public class ListSuite {
  public static Test listSuite(Class<?> type) {
    return suite("quacks like list")
        .test(suite("provides copy constructor")
            .test(copyConstructorStoresAllElementsInOrder(type)))
        .test(suite("overrides get")
            .test(getCanReturnEachElement(type))
            .test(getFailsForIndexAboveBound(type))
            .test(getFailsForIndexBelowBound(type)));
  }

  private static Test copyConstructorStoresAllElementsInOrder(final Class<?> type) {
    return new Case("copy constructor stores all elements in order") {
      public void run() throws Throwable {
        assume(List.class.isAssignableFrom(type));
        run(newArrayList("a", "b", "c"));
        run(newArrayList("a", "c", "b"));
        run(newArrayList("b", "a", "c"));
        run(newArrayList("b", "c", "a"));
        run(newArrayList("c", "a", "b"));
        run(newArrayList("c", "b", "a"));
      }

      private void run(ArrayList<?> order) throws Throwable {
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        List<?> list = (List<?>) constructor.newInstance(copy(order));
        assertEquals(copy(list.toArray()), order.toArray());
      }
    };
  }

  private static Test getCanReturnEachElement(final Class<?> type) {
    return new Case("get can return each element") {
      public void run() throws Throwable {
        assume(List.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        ArrayList<Object> original = newArrayList("a", "b", "c");
        List<?> list = (List<?>) constructor.newInstance(copy(original));
        for (int i = 0; i < original.size(); i++) {
          try {
            assertEquals(list.get(i), original.get(i));
          } catch (IndexOutOfBoundsException e) {
            fail();
          }
        }
      }
    };
  }

  private static Test getFailsForIndexAboveBound(final Class<?> type) {
    return new Case("get fails for index above bound") {
      public void run() throws Throwable {
        assume(List.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        ArrayList<Object> original = newArrayList("a", "b", "c");
        List<?> list = (List<?>) constructor.newInstance(copy(original));
        try {
          list.get(original.size());
          fail();
        } catch (IndexOutOfBoundsException e) {}
      }
    };
  }

  private static Test getFailsForIndexBelowBound(final Class<?> type) {
    return new Case("get fails for index below bound") {
      public void run() throws Throwable {
        assume(List.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        ArrayList<Object> original = newArrayList("a", "b", "c");
        List<?> list = (List<?>) constructor.newInstance(copy(original));
        try {
          list.get(-1);
          fail();
        } catch (IndexOutOfBoundsException e) {}
      }
    };
  }
}
