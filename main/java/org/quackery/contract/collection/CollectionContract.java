package org.quackery.contract.collection;

import static org.quackery.contract.collection.CollectionSuite.collectionSuite;

import java.util.Collection;

import org.quackery.Contract;
import org.quackery.Test;

public final class CollectionContract implements Contract<Class<?>> {
  private CollectionContract() {}

  public static CollectionContract collectionContract(Class<Collection> type) {
    return new CollectionContract();
  }

  public Test test(Class<?> type) {
    return collectionSuite(type);
  }

  public Contract<Class<?>> mutable() {
    return this;
  }
}
