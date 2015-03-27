package org.quackery;

import static org.quackery.contract.collection.CollectionContract.collectionContract;

import java.util.Collection;

public class Contracts {
  public static Contract<Class<?>> quacksLike(Class<Collection> type) {
    return collectionContract(type);
  }
}
