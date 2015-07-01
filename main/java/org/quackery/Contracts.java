package org.quackery;

import java.util.Collection;

import org.quackery.contract.collection.CollectionContract;

public class Contracts {
  public static CollectionContract quacksLike(Class<Collection> type, Collection<?>... erasure) {
    return new CollectionContract();
  }
}
