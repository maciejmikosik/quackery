package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.bugs;
import static org.quackery.contract.collection.Bugs.implementations;
import static org.quackery.testing.Assertions.assertFailure;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;

import org.quackery.Contract;

public class test_quacksLike_collection {
  private final Contract<Class<?>> contract = quacksLike(Collection.class);

  public void accepts_collections() {
    for (Class<?> implementation : implementations(Collection.class)) {
      assertSuccess(contract.test(implementation));
    }
  }

  public void detects_collection_bugs() {
    for (Class<?> bug : bugs(Collection.class)) {
      assertFailure(contract.test(bug));
    }
  }
}
