package org.quackery.contract.collection;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssumptionException.assume;
import static org.quackery.Suite.newSuite;
import static org.quackery.contract.Commons.assumeConstructor;
import static org.quackery.contract.Commons.newArrayList;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.quackery.Case;
import org.quackery.Test;

public class CollectionMutableSuite {
  public static Test collectionMutableSuite(Class<?> type) {
    return newSuite("quacks like mutable collection")
        .test(newSuite("overrides clear")
            .test(clearRemovesElement(type)));
  }

  private static Test clearRemovesElement(final Class<?> type) {
    return new Case("clear empties collection if it has 1 element") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(newArrayList("a"));
        collection.clear();
        assertEquals(collection.toArray(), new Object[] {});
      }
    };
  }
}
