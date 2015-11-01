package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.bug.list.CopyConstructorRemovesDuplicates;
import org.quackery.contract.collection.bug.list.CopyConstructorRemovesLastElement;
import org.quackery.contract.collection.bug.list.CopyConstructorReversesOrder;
import org.quackery.contract.collection.bug.list.CopyConstructorStoresOneElement;
import org.quackery.contract.collection.bug.list.GetReturnsFirstElement;
import org.quackery.contract.collection.bug.list.GetReturnsLastElement;
import org.quackery.contract.collection.bug.list.GetReturnsNull;
import org.quackery.contract.collection.bug.list.GetReturnsNullAboveBound;
import org.quackery.contract.collection.bug.list.GetReturnsNullBelowBound;
import org.quackery.contract.collection.bug.list.factory.FactoryReturnsCollection;

public class test_failing_bugs_of_list {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class);

    for (Class<?> factoryBug : asList(FactoryReturnsCollection.class)) {
      assertFailure(contract.withFactory("create").test(factoryBug));
    }

    for (Class<?> bug : asList(
        CopyConstructorStoresOneElement.class,
        CopyConstructorReversesOrder.class,
        CopyConstructorRemovesLastElement.class,
        CopyConstructorRemovesDuplicates.class,
        GetReturnsFirstElement.class,
        GetReturnsLastElement.class,
        GetReturnsNull.class,
        GetReturnsNullAboveBound.class,
        GetReturnsNullBelowBound.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asListFactory(bug)));
    }
  }
}
