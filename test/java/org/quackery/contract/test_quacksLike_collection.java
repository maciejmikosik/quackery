package org.quackery.contract;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.bug.Bugs.bugs;
import static org.quackery.testing.bug.Bugs.implementations;
import static org.quackery.testing.bug.Expectations.expectFailure;
import static org.quackery.testing.bug.Expectations.expectSuccess;

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
      expectSuccess(contract, implementation);
    }
  }

  public void detects_collection_bugs() {
    for (Class<?> bug : bugs(Collection.class)) {
      expectFailure(contract, bug);
    }
  }

  public void accepts_jdk_collections() {
    expectSuccess(contract, ArrayList.class);
    expectSuccess(contract, LinkedList.class);
    expectSuccess(contract, HashSet.class);
    expectSuccess(contract, TreeSet.class);
  }

  public void detects_alien_types() {
    expectFailure(contract, Object.class);
    expectFailure(contract, String.class);
    expectFailure(contract, Integer.class);
  }
}
