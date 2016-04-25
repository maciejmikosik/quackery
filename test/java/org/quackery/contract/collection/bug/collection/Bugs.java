package org.quackery.contract.collection.bug.collection;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class Bugs {
  public static final List<? extends Class<?>> bugs = unmodifiableList(asList(
      org.quackery.contract.collection.bug.collection.CreatorCreatesEmpty.class,
      org.quackery.contract.collection.bug.collection.CreatorAddsElement.class,
      org.quackery.contract.collection.bug.collection.CreatorAcceptsNull.class,
      org.quackery.contract.collection.bug.collection.CreatorMakesNoDefensiveCopy.class,
      org.quackery.contract.collection.bug.collection.CreatorModifiesArgument.class,
      org.quackery.contract.collection.bug.collection.CreatorCreatesFixed.class,
      org.quackery.contract.collection.bug.collection.ToArrayReturnsEmpty.class,
      org.quackery.contract.collection.bug.collection.ToArrayReturnsUnknownElement.class,
      org.quackery.contract.collection.bug.collection.ToArrayReturnsNull.class,
      org.quackery.contract.collection.bug.collection.SizeReturnsZero.class,
      org.quackery.contract.collection.bug.collection.SizeReturnsOne.class,
      org.quackery.contract.collection.bug.collection.IsEmptyReturnsTrue.class,
      org.quackery.contract.collection.bug.collection.IsEmptyReturnsFalse.class,
      org.quackery.contract.collection.bug.collection.IsEmptyNegates.class,
      org.quackery.contract.collection.bug.collection.ContainsReturnsTrue.class,
      org.quackery.contract.collection.bug.collection.ContainsReturnsFalse.class,
      org.quackery.contract.collection.bug.collection.ContainsNegates.class,
      org.quackery.contract.collection.bug.collection.IteratorReturnsNull.class,
      org.quackery.contract.collection.bug.collection.IteratorIsEmpty.class,
      org.quackery.contract.collection.bug.collection.IteratorHasUnknownElement.class,
      org.quackery.contract.collection.bug.collection.IteratorHasUnknownElementIfCollectionIsEmpty.class,
      org.quackery.contract.collection.bug.collection.IteratorRepeatsFirstElementInfinitely.class,
      org.quackery.contract.collection.bug.collection.IteratorInsertsFirstElement.class,
      org.quackery.contract.collection.bug.collection.IteratorInsertsLastElement.class,
      org.quackery.contract.collection.bug.collection.IteratorSkipsFirstElement.class,
      org.quackery.contract.collection.bug.collection.IteratorHasNextAlways.class,
      org.quackery.contract.collection.bug.collection.IteratorHasNextNever.class,
      org.quackery.contract.collection.bug.collection.IteratorHasNextNegates.class,
      org.quackery.contract.collection.bug.collection.IteratorHasNextIfCollectionIsEmpty.class,
      org.quackery.contract.collection.bug.collection.IteratorHasNextIfCollectionIsNotEmpty.class,
      org.quackery.contract.collection.bug.collection.IteratorNextReturnsUnknownElementAfterTraversing.class,
      org.quackery.contract.collection.bug.collection.IteratorNextReturnsNullAfterTraversing.class));
}
