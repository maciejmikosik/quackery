package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.bug.list.immutable.AddAllIntAddsElements;
import org.quackery.contract.collection.bug.list.immutable.AddAllIntDoesNotThrowException;
import org.quackery.contract.collection.bug.list.immutable.AddIntAddsElement;
import org.quackery.contract.collection.bug.list.immutable.AddIntDoesNotThrowException;
import org.quackery.contract.collection.bug.list.immutable.ListIteratorAddDoesNotThrowException;
import org.quackery.contract.collection.bug.list.immutable.ListIteratorAddsElement;
import org.quackery.contract.collection.bug.list.immutable.ListIteratorRemoveDoesNotThrowException;
import org.quackery.contract.collection.bug.list.immutable.ListIteratorRemovesElement;
import org.quackery.contract.collection.bug.list.immutable.ListIteratorSetDoesNotThrowException;
import org.quackery.contract.collection.bug.list.immutable.ListIteratorSetsElement;
import org.quackery.contract.collection.bug.list.immutable.RemoveIntDoesNotThrowException;
import org.quackery.contract.collection.bug.list.immutable.RemoveIntRemovesElement;
import org.quackery.contract.collection.bug.list.immutable.SetDoesNotThrowException;
import org.quackery.contract.collection.bug.list.immutable.SetSetsElement;

public class test_failing_bugs_of_immutable_list {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .immutable();
    for (Class<?> bug : asList(
        AddAllIntAddsElements.class,
        AddAllIntDoesNotThrowException.class,
        SetSetsElement.class,
        SetDoesNotThrowException.class,
        AddIntAddsElement.class,
        AddIntDoesNotThrowException.class,
        RemoveIntRemovesElement.class,
        RemoveIntDoesNotThrowException.class,
        ListIteratorRemovesElement.class,
        ListIteratorRemoveDoesNotThrowException.class,
        ListIteratorSetsElement.class,
        ListIteratorSetDoesNotThrowException.class,
        ListIteratorAddsElement.class,
        ListIteratorAddDoesNotThrowException.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
