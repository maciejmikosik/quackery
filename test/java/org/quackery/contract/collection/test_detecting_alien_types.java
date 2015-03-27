package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;

public class test_detecting_alien_types {
  private static final List<Class<?>> alienTypes = unmodifiableList(asList(
      Object.class,
      String.class,
      Integer.class,
      HasCollectionConstructors.class));
  private Contract<Class<?>> contract;

  public void are_detected_by_collection_contract() {
    contract = quacksLike(Collection.class);
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }

  public void are_detected_by_mutable_collection_contract() {
    contract = quacksLike(Collection.class)
        .mutable();
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }

  @SuppressWarnings("unused")
  private static class HasCollectionConstructors {
    public HasCollectionConstructors() {}

    public HasCollectionConstructors(Collection<?> collection) {
      if (collection == null) {
        throw new NullPointerException();
      }
    }
  }
}
