package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.bug.list.mutable.AddAddsAtTheBegin;
import org.quackery.contract.collection.bug.list.mutable.AddIntAddsAtTheEnd;
import org.quackery.contract.collection.bug.list.mutable.AddNotAddsDuplicatedElement;
import org.quackery.contract.collection.bug.list.mutable.AddReturnsFalse;
import org.quackery.contract.collection.bug.list.mutable.SetAddsElement;
import org.quackery.contract.collection.bug.list.mutable.SetIndexesFromEnd;
import org.quackery.contract.collection.bug.list.mutable.SetReturnsInsertedElement;

public class test_failing_bugs_of_mutable_list {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .mutable();
    for (Class<?> bug : asList(
        AddAddsAtTheBegin.class,
        AddReturnsFalse.class,
        AddNotAddsDuplicatedElement.class,
        SetAddsElement.class,
        SetReturnsInsertedElement.class,
        SetIndexesFromEnd.class,
        AddIntAddsAtTheEnd.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asListFactory(bug)));
    }
  }
}
