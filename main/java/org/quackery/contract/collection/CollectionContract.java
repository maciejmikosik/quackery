package org.quackery.contract.collection;

import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.suite.CollectionMutableSuite.collectionMutableSuite;
import static org.quackery.contract.collection.suite.CollectionSuite.collectionSuite;
import static org.quackery.contract.collection.suite.ListMutableSuite.listMutableSuite;
import static org.quackery.contract.collection.suite.ListSuite.listSuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quackery.Contract;
import org.quackery.Test;

public final class CollectionContract implements Contract<Class<?>> {
  private final Class<?> supertype;
  private final boolean mutable;

  private CollectionContract(Class<?> supertype, boolean mutable) {
    this.supertype = supertype;
    this.mutable = mutable;
  }

  public static CollectionContract collectionContract(Class<Collection> supertype,
      Collection<?>... erasure) {
    return new CollectionContract(supertype, false);
  }

  public static CollectionContract collectionContract(Class<List> supertype, List<?>... erasure) {
    return new CollectionContract(supertype, false);
  }

  public Test test(Class<?> type) {
    List<Test> suites = new ArrayList<>();
    suites.add(collectionSuite(type));
    if (mutable) {
      suites.add(collectionMutableSuite(type));
    }
    if (supertype == List.class) {
      suites.add(listSuite(type));
    }
    if (supertype == List.class && mutable) {
      suites.add(listMutableSuite(type));
    }
    return suite(name(type)).testAll(suites);
  }

  private String name(Class<?> type) {
    StringBuilder builder = new StringBuilder();
    builder.append(type.getName() + " quacks like");
    if (mutable) {
      builder.append(" mutable");
    }
    builder.append(" ").append(supertype.getSimpleName().toLowerCase());
    return builder.toString();
  }

  public Contract<Class<?>> mutable() {
    return new CollectionContract(supertype, true);
  }
}
