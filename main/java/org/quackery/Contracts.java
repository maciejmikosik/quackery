package org.quackery;

import static org.quackery.contract.collection.CollectionContract.collectionContract;

import java.util.Collection;

import org.quackery.contract.collection.CollectionContract;

public class Contracts {
  public static CollectionContract quacksLike(Class<Collection> type) {
    return collectionContract(type);
  }
}
