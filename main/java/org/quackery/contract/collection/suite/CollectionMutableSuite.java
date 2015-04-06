package org.quackery.contract.collection.suite;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;

import java.util.Collection;

import org.quackery.Case;
import org.quackery.Test;
import org.quackery.contract.collection.Creator;

public class CollectionMutableSuite {
  public static Test clearRemovesElement(final Creator creator) {
    return new Case("empties collection if it has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        collection.clear();
        assertEquals(collection.toArray(), new Object[] {});
      }
    };
  }
}
