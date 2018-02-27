package org.quackery.contract.collection.bug.collection.mutable;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class CollectionMutableBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesSwallowsException.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesThrowsException.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesThrowsInverted.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesIgnoresSecondCall.class,
      org.quackery.contract.collection.bug.collection.mutable.AddHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.AddReturnsNegation.class,
      org.quackery.contract.collection.bug.collection.mutable.AddAllHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.RemoveHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.ClearHasNoEffect.class));
}
