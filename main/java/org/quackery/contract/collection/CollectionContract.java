package org.quackery.contract.collection;

import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.CollectionMutableSuite.collectionMutableSuite;
import static org.quackery.contract.collection.CollectionSuite.collectionSuite;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;
import org.quackery.Suite;
import org.quackery.Test;

public final class CollectionContract implements Contract<Class<?>> {
  private final boolean mutable;

  private CollectionContract(boolean mutable) {
    this.mutable = mutable;
  }

  public static CollectionContract collectionContract(Class<Collection> type,
      Collection<?>... erasure) {
    return new CollectionContract(false);
  }

  public static CollectionContract collectionContract(Class<List> type, List<?>... erasure) {
    return new CollectionContract(false);
  }

  public Test test(Class<?> type) {
    Suite suite = mutable
        ? suite("quacks like mutable collection")
            .test(collectionSuite(type))
            .test(collectionMutableSuite(type))
        : suite("quacks like collection")
            .test(collectionSuite(type));
    return suite(type.getName() + " " + suite.name).testAll(suite.tests);
  }

  public Contract<Class<?>> mutable() {
    return new CollectionContract(true);
  }
}
