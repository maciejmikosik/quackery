package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.AssertionException.assertThat;
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
            .test(copyConstructorStoresAllElements(type)));
  }

  private static Test copyConstructorStoresAllElements(final Class<?> type) {
    return new Case("copy constructor stores all elements") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        ArrayList<String> original = newArrayList("a", "b", "c", "d", "e");
        Collection<?> collection = (Collection<?>) constructor.newInstance(copy(original));
        Object[] array = copy(collection.toArray());
        assertThat(array != null && asList(array).containsAll(original));
      }
    };
  }
}
