package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.bugs;
import static org.quackery.contract.collection.Bugs.implementations;
import static org.quackery.testing.Assertions.assertFailure;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import org.quackery.Contract;

public class test_quacksLike_collection_mutable {
  private final Contract<Class<?>> contract = quacksLike(Collection.class).mutable();

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

  public void detects_collection_mutable_bugs() {
    for (Class<?> bug : bugs(Collection.class, Mutable.class)) {
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
