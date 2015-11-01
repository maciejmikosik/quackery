package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.collectionImmutableBugs;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;

public class test_failing_bugs_of_immutable_collection {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .immutable();

    for (Class<?> bug : collectionImmutableBugs) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
