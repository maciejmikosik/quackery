package org.quackery;

import static org.quackery.Quacks.quacksLike;
import static org.quackery.testing.bug.Bugs.bugs;
import static org.quackery.testing.bug.Bugs.implementations;
import static org.quackery.testing.bug.Expectations.expectFailure;
import static org.quackery.testing.bug.Expectations.expectSuccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

public class describe_Quacks_quacksLike_collection {
  private final Contract<Class<?>> contract = quacksLike(Collection.class);

  public void accepts_mutable_list() throws Throwable {
    for (Class<?> implementation : implementations(Collection.class)) {
      expectSuccess(contract, implementation);
    }
  }

  public void detects_collection_bugs() throws Throwable {
    for (Class<?> bug : bugs(Collection.class)) {
      expectFailure(contract, bug);
    }
  }

  public void accepts_jdk_collections() throws Throwable {
    expectSuccess(contract, ArrayList.class);
    expectSuccess(contract, LinkedList.class);
    expectSuccess(contract, HashSet.class);
    expectSuccess(contract, TreeSet.class);
  }

  public void detects_alien_types() throws Throwable {
    expectFailure(contract, Object.class);
    expectFailure(contract, String.class);
    expectFailure(contract, Integer.class);
  }
}
