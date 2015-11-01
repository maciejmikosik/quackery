package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;

import org.quackery.contract.collection.bug.collection.ContainsNegates;
import org.quackery.contract.collection.bug.collection.ContainsReturnsFalse;
import org.quackery.contract.collection.bug.collection.ContainsReturnsTrue;
import org.quackery.contract.collection.bug.collection.CreatorAcceptsNull;
import org.quackery.contract.collection.bug.collection.CreatorAddsElement;
import org.quackery.contract.collection.bug.collection.CreatorCreatesEmpty;
import org.quackery.contract.collection.bug.collection.CreatorCreatesFixed;
import org.quackery.contract.collection.bug.collection.CreatorMakesNoDefensiveCopy;
import org.quackery.contract.collection.bug.collection.CreatorModifiesArgument;
import org.quackery.contract.collection.bug.collection.IsEmptyNegates;
import org.quackery.contract.collection.bug.collection.IsEmptyReturnsFalse;
import org.quackery.contract.collection.bug.collection.IsEmptyReturnsTrue;
import org.quackery.contract.collection.bug.collection.IteratorHasNextAlways;
import org.quackery.contract.collection.bug.collection.IteratorHasNextIfCollectionIsEmpty;
import org.quackery.contract.collection.bug.collection.IteratorHasNextIfCollectionIsNotEmpty;
import org.quackery.contract.collection.bug.collection.IteratorHasNextNegates;
import org.quackery.contract.collection.bug.collection.IteratorHasNextNever;
import org.quackery.contract.collection.bug.collection.IteratorHasUnknownElement;
import org.quackery.contract.collection.bug.collection.IteratorHasUnknownElementIfCollectionIsEmpty;
import org.quackery.contract.collection.bug.collection.IteratorInsertsFirstElement;
import org.quackery.contract.collection.bug.collection.IteratorInsertsLastElement;
import org.quackery.contract.collection.bug.collection.IteratorIsEmpty;
import org.quackery.contract.collection.bug.collection.IteratorNextReturnsNullAfterTraversing;
import org.quackery.contract.collection.bug.collection.IteratorNextReturnsUnknownElementAfterTraversing;
import org.quackery.contract.collection.bug.collection.IteratorRepeatsFirstElementInfinitely;
import org.quackery.contract.collection.bug.collection.IteratorReturnsNull;
import org.quackery.contract.collection.bug.collection.IteratorSkipsFirstElement;
import org.quackery.contract.collection.bug.collection.SizeReturnsOne;
import org.quackery.contract.collection.bug.collection.SizeReturnsZero;
import org.quackery.contract.collection.bug.collection.ToArrayReturnsEmpty;
import org.quackery.contract.collection.bug.collection.ToArrayReturnsNull;
import org.quackery.contract.collection.bug.collection.ToArrayReturnsUnknownElement;
import org.quackery.contract.collection.bug.collection.constructor.CopyConstructorIsHidden;
import org.quackery.contract.collection.bug.collection.constructor.CopyConstructorIsMissing;
import org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorAddsElement;
import org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorIsHidden;
import org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorIsMissing;
import org.quackery.contract.collection.bug.collection.factory.FactoryAcceptsObject;
import org.quackery.contract.collection.bug.collection.factory.FactoryHasDifferentName;
import org.quackery.contract.collection.bug.collection.factory.FactoryIsHidden;
import org.quackery.contract.collection.bug.collection.factory.FactoryIsMissing;
import org.quackery.contract.collection.bug.collection.factory.FactoryIsNotStatic;
import org.quackery.contract.collection.bug.collection.factory.FactoryReturnsObject;

public class test_failing_bugs_of_collection {

  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class);

    for (Class<?> constructorBug : asList(
        DefaultConstructorIsMissing.class,
        DefaultConstructorIsHidden.class,
        DefaultConstructorAddsElement.class,
        CopyConstructorIsMissing.class,
        CopyConstructorIsHidden.class)) {
      assertFailure(contract.test(constructorBug));
    }

    for (Class<?> factoryBug : asList(
        FactoryIsMissing.class,
        FactoryIsHidden.class,
        FactoryIsNotStatic.class,
        FactoryReturnsObject.class,
        FactoryHasDifferentName.class,
        FactoryAcceptsObject.class)) {
      assertFailure(contract.withFactory("create").test(factoryBug));
    }

    for (Class<?> bug : asList(
        CreatorCreatesEmpty.class,
        CreatorAddsElement.class,
        CreatorAcceptsNull.class,
        CreatorMakesNoDefensiveCopy.class,
        CreatorModifiesArgument.class,
        CreatorCreatesFixed.class,
        ToArrayReturnsEmpty.class,
        ToArrayReturnsUnknownElement.class,
        ToArrayReturnsNull.class,
        SizeReturnsZero.class,
        SizeReturnsOne.class,
        IsEmptyReturnsTrue.class,
        IsEmptyReturnsFalse.class,
        IsEmptyNegates.class,
        ContainsReturnsTrue.class,
        ContainsReturnsFalse.class,
        ContainsNegates.class,
        IteratorReturnsNull.class,
        IteratorIsEmpty.class,
        IteratorHasUnknownElement.class,
        IteratorHasUnknownElementIfCollectionIsEmpty.class,
        IteratorRepeatsFirstElementInfinitely.class,
        IteratorInsertsFirstElement.class,
        IteratorInsertsLastElement.class,
        IteratorSkipsFirstElement.class,
        IteratorHasNextAlways.class,
        IteratorHasNextNever.class,
        IteratorHasNextNegates.class,
        IteratorHasNextIfCollectionIsEmpty.class,
        IteratorHasNextIfCollectionIsNotEmpty.class,
        IteratorNextReturnsUnknownElementAfterTraversing.class,
        IteratorNextReturnsNullAfterTraversing.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }
}
