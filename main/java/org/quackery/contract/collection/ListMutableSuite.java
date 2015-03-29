package org.quackery.contract.collection;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.assertThat;
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

public class ListMutableSuite {
  public static Test listMutableSuite(Class<?> type) {
    return suite("quacks like mutable list")
        .test(suite("overrides add")
            .test(addAddsElementAtTheEnd(type))
            .test(addReturnsTrue(type)));
  }

  private static Test addAddsElementAtTheEnd(final Class<?> type) {
    return new Case("add adds element at the end") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        ArrayList<Object> original = newArrayList("a", "b", "c");
        List<Object> list = (List<Object>) constructor.newInstance(copy(original));
        original.add("d");
        list.add("d");
        assertEquals(copy(list.toArray()), original.toArray());
      }
    };
  }

  private static Test addReturnsTrue(final Class<?> type) {
    return new Case("add returns true") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        List<Object> list = (List<Object>) constructor.newInstance(newArrayList("a", "b", "c"));
        assertThat(list.add("d"));
      }
    };
  }
}
