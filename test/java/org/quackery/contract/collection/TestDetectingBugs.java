package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.TestingContracts.assertFailure;

import java.util.Collection;
import java.util.List;

public class TestDetectingBugs {
  public void detects_bugs() {
    for (boolean immutable : asList(true, false)) {
      for (boolean forbiddingNull : asList(true, false)) {
        for (Class<?> implementing : asList(Collection.class, List.class)) {
          Bugs bugs = new Bugs();
          CollectionContract contract = quacksLike(Collection.class);
          if (immutable) {
            bugs = bugs.immutable();
            contract = contract.immutable();
          }
          if (forbiddingNull) {
            bugs = bugs.forbiddingNull();
            contract = contract.forbidding(null);
          }
          if (implementing == List.class) {
            bugs = bugs.list();
            contract = contract.implementing(List.class);
          }
          for (Class<?> bug : bugs.get()) {
            assertFailure(contract.test(bug));
          }
          for (Class<?> bug : bugs.factory("create").get()) {
            assertFailure(contract.withFactory("create").test(bug));
          }
        }
      }
    }
  }
}
