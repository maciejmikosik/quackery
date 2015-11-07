package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

public class test_detecting_bugs {
  public void detects_collection_bugs() {
    CollectionContract contract = quacksLike(Collection.class);
    assertFailures(new Bugs(), contract);
  }

  public void detects_collection_mutable_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .mutable();
    assertFailures(new Bugs().mutable(), contract);
  }

  public void detects_collection_immutable_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .immutable();
    assertFailures(new Bugs().immutable(), contract);
  }

  public void detects_collection_forbidding_null_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .forbidding(null);
    assertFailures(new Bugs().forbiddingNull(), contract);
  }

  public void detects_collection_mutable_forbidding_null_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .mutable()
        .forbidding(null);
    assertFailures(new Bugs().mutable().forbiddingNull(), contract);
  }

  public void detects_list_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class);
    assertFailures(new Bugs().list(), contract);
  }

  public void detects_list_mutable_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .mutable();
    assertFailures(new Bugs().list().mutable(), contract);
  }

  public void detects_list_immutable_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .immutable();
    assertFailures(new Bugs().list().immutable(), contract);
  }

  private static void assertFailures(Bugs bugs, CollectionContract contract) {
    for (Class<?> bug : bugs.get()) {
      assertFailure(contract.test(bug));
    }
    for (Class<?> bug : bugs.factory().get()) {
      assertFailure(contract.withFactory("create").test(bug));
    }
  }
}
