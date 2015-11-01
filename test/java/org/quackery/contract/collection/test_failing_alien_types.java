package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;
import org.quackery.contract.collection.bug.alien.FakeCollection;
import org.quackery.contract.collection.bug.alien.HasCollectionConstructors;

public class test_failing_alien_types {
  private static final List<Class<?>> alienTypes = unmodifiableList(asList(
      Object.class,
      String.class,
      Integer.class,
      HasCollectionConstructors.class,
      FakeCollection.class));
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

  public void are_detected_by_list_contract() {
    contract = quacksLike(Collection.class)
        .implementing(List.class);
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }

  public void are_detected_by_mutable_list_contract() {
    contract = quacksLike(Collection.class)
        .implementing(List.class)
        .mutable();
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }
}
