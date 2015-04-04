package org.quackery.contract.collection;

import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Flags.clean;
import static org.quackery.contract.collection.Flags.onlyIf;
import static org.quackery.contract.collection.suite.CollectionMutableSuite.collectionMutableSuite;
import static org.quackery.contract.collection.suite.CollectionSuite.collectionSuite;
import static org.quackery.contract.collection.suite.ListMutableSuite.listMutableSuite;
import static org.quackery.contract.collection.suite.ListSuite.listSuite;

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
    boolean isList = supertype == List.class;
    return clean(suite(name(type))
        .test(collectionSuite(type))
        .test(onlyIf(mutable, collectionMutableSuite(type)))
        .test(onlyIf(isList, listSuite(type)))
        .test(onlyIf(isList && mutable, listMutableSuite(type))));
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
