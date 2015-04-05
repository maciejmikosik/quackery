package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.bugs;
import static org.quackery.testing.Assertions.assertFailure;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;

public class test_contract_for_list {
  private final Contract<Class<?>> contract = quacksLike(List.class);

  public void accepts_lists() {
    assertSuccess(contract.test(MutableList.class));
  }

  public void detects_collection_bugs() {
    for (Class<?> bug : bugs(MutableList.class, Collection.class)) {
      assertFailure(contract.test(bug));
    }
  }

  public void detects_list_bugs() {
    for (Class<?> bug : bugs(MutableList.class, List.class)) {
      assertFailure(contract.test(bug));
    }
  }
}
