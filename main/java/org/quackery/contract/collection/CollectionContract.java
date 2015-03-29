package org.quackery.contract.collection;

import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.CollectionMutableSuite.collectionMutableSuite;
import static org.quackery.contract.collection.CollectionSuite.collectionSuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quackery.Contract;
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
    List<Test> suites = new ArrayList<>();
    suites.add(collectionSuite(type));
    if (mutable) {
      suites.add(collectionMutableSuite(type));
    }
    return suite(name(type)).testAll(suites);
  }

  private String name(Class<?> type) {
    StringBuilder builder = new StringBuilder();
    builder.append(type.getName() + " quacks like");
    if (mutable) {
      builder.append(" mutable");
    }
    builder.append(" collection");
    return builder.toString();
  }

  public Contract<Class<?>> mutable() {
    return new CollectionContract(true);
  }
}
