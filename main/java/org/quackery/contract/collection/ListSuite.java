package org.quackery.contract.collection;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssumptionException.assume;
import static org.quackery.Suite.suite;
import static org.quackery.contract.Commons.assumeConstructor;
import static org.quackery.contract.Commons.copy;
import static org.quackery.contract.Commons.newArrayList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import org.quackery.Case;
import org.quackery.Test;

public class ListSuite {
  public static Test listSuite(Class<?> type) {
    return suite("quacks like list")
        .test(suite("provides copy constructor")
            .test(copyConstructorStoresAllElementsInOrder(type)));
  }

  private static Test copyConstructorStoresAllElementsInOrder(final Class<?> type) {
    return new Case("copy constructor stores all elements in order") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        run(newArrayList("a", "b", "c"));
        run(newArrayList("a", "c", "b"));
        run(newArrayList("b", "a", "c"));
        run(newArrayList("b", "c", "a"));
        run(newArrayList("c", "a", "b"));
        run(newArrayList("c", "b", "a"));
      }

      private void run(ArrayList<?> order) throws Throwable {
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(copy(order));
        assertEquals(copy(collection.toArray()), order.toArray());
      }
    };
  }
}
