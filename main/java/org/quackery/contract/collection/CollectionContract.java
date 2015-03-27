package org.quackery.contract.collection;

import static org.quackery.Suite.newSuite;
import static org.quackery.contract.collection.CollectionMutableSuite.collectionMutableSuite;
import static org.quackery.contract.collection.CollectionSuite.collectionSuite;

import java.util.Collection;

import org.quackery.Contract;
import org.quackery.Test;

public final class CollectionContract implements Contract<Class<?>> {
  private final boolean mutable;

  private CollectionContract(boolean mutable) {
    this.mutable = mutable;
  }

  public static CollectionContract collectionContract(Class<Collection> type) {
    return new CollectionContract(false);
  }

  public Test test(Class<?> type) {
    return mutable
        ? newSuite(type.getName() + " quacks like mutable collection")
            .test(collectionSuite(type))
            .test(collectionMutableSuite(type))
        : collectionSuite(type);
  }

  public Contract<Class<?>> mutable() {
    return new CollectionContract(true);
  }
}
