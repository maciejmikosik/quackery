package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.collectionMutableBugs;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;

public class test_failing_bugs_of_mutable_collection {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .mutable();

    for (Class<?> bug : collectionMutableBugs) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
