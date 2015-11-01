package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;

import org.quackery.contract.collection.bug.collection.mutable.AddAllHasNoEffect;
import org.quackery.contract.collection.bug.collection.mutable.AddHasNoEffect;
import org.quackery.contract.collection.bug.collection.mutable.AddReturnsNegation;
import org.quackery.contract.collection.bug.collection.mutable.ClearHasNoEffect;
import org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesHasNoEffect;
import org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesIgnoresSecondCall;
import org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesSwallowsException;
import org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesThrowsException;
import org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesThrowsInverted;
import org.quackery.contract.collection.bug.collection.mutable.RemoveHasNoEffect;

public class test_failing_bugs_of_mutable_collection {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .mutable();
    for (Class<?> bug : asList(
        IteratorRemovesHasNoEffect.class,
        IteratorRemovesSwallowsException.class,
        IteratorRemovesThrowsException.class,
        IteratorRemovesThrowsInverted.class,
        IteratorRemovesIgnoresSecondCall.class,
        AddHasNoEffect.class,
        AddReturnsNegation.class,
        AddAllHasNoEffect.class,
        RemoveHasNoEffect.class,
        ClearHasNoEffect.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
