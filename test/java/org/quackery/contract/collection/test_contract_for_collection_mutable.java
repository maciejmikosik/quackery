package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.bugs;
import static org.quackery.contract.collection.Bugs.implementations;
import static org.quackery.testing.Assertions.assertFailure;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;

import org.quackery.Contract;

public class test_contract_for_collection_mutable {
  private final Contract<Class<?>> contract = quacksLike(Collection.class).mutable();

  public void accepts_mutable_collections() {
    for (Class<?> implementation : implementations(Collection.class, Mutable.class)) {
      assertSuccess(contract.test(implementation));
    }
  }

  public void detects_collection_bugs() {
    for (Class<?> bug : bugs(MutableList.class, Collection.class)) {
      assertFailure(contract.test(bug));
    }
  }

  public void detects_mutable_collection_bugs() {
    for (Class<?> bug : bugs(MutableList.class, Collection.class, Mutable.class)) {
      assertFailure(contract.test(bug));
    }
  }
}
