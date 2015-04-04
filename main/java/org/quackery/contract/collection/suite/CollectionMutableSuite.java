package org.quackery.contract.collection.suite;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Assumptions.assumeCreateCollection;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;

import java.util.Collection;

import org.quackery.Case;
import org.quackery.Test;

public class CollectionMutableSuite {
  public static Test collectionMutableSuite(Class<?> type) {
    return suite("quacks like mutable collection")
        .test(suite("overrides clear")
            .test(clearRemovesElement(type)));
  }

  private static Test clearRemovesElement(final Class<?> type) {
    return new Case("clear empties collection if it has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList(a));
        collection.clear();
        assertEquals(collection.toArray(), new Object[] {});
      }
    };
  }
}
