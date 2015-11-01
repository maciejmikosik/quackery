package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;

import org.quackery.contract.collection.bug.collection.immutable.AddAddsElement;
import org.quackery.contract.collection.bug.collection.immutable.AddAllAddsElements;
import org.quackery.contract.collection.bug.collection.immutable.AddAllDoesNotThrowException;
import org.quackery.contract.collection.bug.collection.immutable.AddDoesNotThrowException;
import org.quackery.contract.collection.bug.collection.immutable.ClearClearsElements;
import org.quackery.contract.collection.bug.collection.immutable.ClearDoesNotThrowException;
import org.quackery.contract.collection.bug.collection.immutable.IteratorDoesNotThrowException;
import org.quackery.contract.collection.bug.collection.immutable.IteratorRemovesElement;
import org.quackery.contract.collection.bug.collection.immutable.RemoveAllDoesNotThrowException;
import org.quackery.contract.collection.bug.collection.immutable.RemoveAllRemovesElements;
import org.quackery.contract.collection.bug.collection.immutable.RemoveDoesNotThrowException;
import org.quackery.contract.collection.bug.collection.immutable.RemoveRemovesElement;
import org.quackery.contract.collection.bug.collection.immutable.RetainAllDoesNotThrowException;
import org.quackery.contract.collection.bug.collection.immutable.RetainAllRetainsElements;

public class test_failing_bugs_of_immutable_collection {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .immutable();
    for (Class<?> bug : asList(
        IteratorRemovesElement.class,
        IteratorDoesNotThrowException.class,
        AddAddsElement.class,
        AddDoesNotThrowException.class,
        RemoveRemovesElement.class,
        RemoveDoesNotThrowException.class,
        AddAllAddsElements.class,
        AddAllDoesNotThrowException.class,
        RemoveAllRemovesElements.class,
        RemoveAllDoesNotThrowException.class,
        RetainAllRetainsElements.class,
        RetainAllDoesNotThrowException.class,
        ClearClearsElements.class,
        ClearDoesNotThrowException.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
