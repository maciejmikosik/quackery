package org.quackery;

import java.util.Collection;

import org.quackery.contract.CollectionContract;

public class Contracts {
  @SuppressWarnings("rawtypes")
  public static Contract<Class<?>> quacksLike(Class<Collection> type) {
    return CollectionContract.quacksLike(type);
  }
}
