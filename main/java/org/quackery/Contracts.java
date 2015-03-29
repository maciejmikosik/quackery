package org.quackery;

import static org.quackery.contract.collection.CollectionContract.collectionContract;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.CollectionContract;

public class Contracts {
  public static CollectionContract quacksLike(Class<Collection> type, Collection<?>... erasure) {
    return collectionContract(type);
  }

  public static CollectionContract quacksLike(Class<List> type, List<?>... erasure) {
    return collectionContract(type);
  }
}
