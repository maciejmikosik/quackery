package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Bugs.collectionBugs;
import static org.quackery.contract.collection.Bugs.collectionConstructorBugs;
import static org.quackery.contract.collection.Bugs.collectionFactoryBugs;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;

public class test_failing_bugs_of_collection {

  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class);

    for (Class<?> bug : collectionConstructorBugs) {
      assertFailure(contract.test(bug));
    }

    for (Class<?> bug : collectionFactoryBugs) {
      assertFailure(contract.withFactory("create").test(bug));
    }

    for (Class<?> bug : collectionBugs) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
