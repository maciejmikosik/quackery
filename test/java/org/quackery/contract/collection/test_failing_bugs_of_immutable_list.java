package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.listImmutableBugs;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

public class test_failing_bugs_of_immutable_list {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .immutable();

    for (Class<?> bug : listImmutableBugs) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
