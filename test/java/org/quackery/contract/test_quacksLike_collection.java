package org.quackery.contract;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertFailure;
import static org.quackery.testing.Assertions.assertSuccess;
import static org.quackery.testing.bug.Bugs.bugs;
import static org.quackery.testing.bug.Bugs.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import org.quackery.Contract;

public class test_quacksLike_collection {
  private final Contract<Class<?>> contract = quacksLike(Collection.class);

  public void accepts_mutable_list() {
    for (Class<?> implementation : implementations(Collection.class)) {
      assertSuccess(contract.test(implementation));
    }
  }

  public void detects_collection_bugs() {
    for (Class<?> bug : bugs(Collection.class)) {
      assertFailure(contract.test(bug));
    }
  }

  public void accepts_jdk_collections() {
    assertSuccess(contract.test(ArrayList.class));
    assertSuccess(contract.test(LinkedList.class));
    assertSuccess(contract.test(HashSet.class));
    assertSuccess(contract.test(TreeSet.class));
  }

  public void detects_alien_types() {
    assertFailure(contract.test(Object.class));
    assertFailure(contract.test(String.class));
    assertFailure(contract.test(Integer.class));
  }
}
