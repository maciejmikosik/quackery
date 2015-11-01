package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.listMutableBugs;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

public class test_failing_bugs_of_mutable_list {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .mutable();

    for (Class<?> bug : listMutableBugs) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asListFactory(bug)));
    }
  }
}
