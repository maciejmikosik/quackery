package org.quackery.contract.collection;

import static java.util.Collections.unmodifiableList;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.contract.collection.Factories.asListFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Bugs {
  private final boolean isList, hasFactory, isMutable, isImmutable, isForbiddingNull;

  private Bugs(boolean isList, boolean hasFactory,
      boolean isMutable, boolean isImmutable, boolean isForbiddingNull) {
    this.isList = isList;
    this.hasFactory = hasFactory;
    this.isMutable = isMutable;
    this.isImmutable = isImmutable;
    this.isForbiddingNull = isForbiddingNull;
  }

  public Bugs() {
    this(false, false, false, false, false);
  }

  public Bugs list() {
    return new Bugs(true, hasFactory, isMutable, isImmutable, isForbiddingNull);
  }

  public Bugs factory() {
    return new Bugs(isList, true, isMutable, isImmutable, isForbiddingNull);
  }

  public Bugs mutable() {
    return new Bugs(isList, hasFactory, true, isImmutable, isForbiddingNull);
  }

  public Bugs immutable() {
    return new Bugs(isList, hasFactory, isMutable, true, isForbiddingNull);
  }

  public Bugs forbiddingNull() {
    return new Bugs(isList, hasFactory, isMutable, isImmutable, true);
  }

  public List<Class<?>> get() {
    List<Class<?>> bugs = new LinkedList<>();

    bugs.addAll(alienTypes);
    bugs.addAll(hasFactory
        ? collectionFactoryBugs
        : collectionConstructorBugs);
    bugs.addAll(asFactoriesIf(hasFactory, collectionBugs));
    if (isMutable) {
      bugs.addAll(asFactoriesIf(hasFactory, collectionMutableBugs));
    }
    if (isImmutable) {
      bugs.addAll(asFactoriesIf(hasFactory, collectionImmutableBugs));
    }
    if (isForbiddingNull) {
      bugs.addAll(asFactoriesIf(hasFactory, collectionForbiddingNullBugs));
      if (isMutable) {
        bugs.addAll(asFactoriesIf(hasFactory, collectionMutableForbiddingNullBugs));
      }
    } else {
      bugs.addAll(asFactoriesIf(hasFactory, collectionAllowingNullBugs));
      if (isMutable) {
        bugs.addAll(asFactoriesIf(hasFactory, collectionMutableAllowingNullBugs));
      }
    }
    if (isList) {
      if (hasFactory) {
        bugs.addAll(listFactoryBugs);
      }
      bugs.addAll(asFactoriesIf(hasFactory, listBugs));
      if (isMutable) {
        bugs.addAll(asFactoriesIf(hasFactory, listMutableBugs));
      }
      if (isImmutable) {
        bugs.addAll(asFactoriesIf(hasFactory, listImmutableBugs));
      }
    }

    return unmodifiableList(bugs);
  }

  private static List<Class<?>> asFactoriesIf(boolean hasFactory, List<Class<?>> bugs) {
    return hasFactory
        ? asFactories(bugs)
        : bugs;
  }

  private static List<Class<?>> asFactories(List<Class<?>> bugs) {
    List<Class<?>> factoryBugs = new LinkedList<>();
    for (Class<?> bug : bugs) {
      factoryBugs.add(bug.isInstance(List.class)
          ? asListFactory(bug)
          : asCollectionFactory(bug));
    }
    return factoryBugs;
  }

  private static final List<Class<?>> alienTypes = classes(
      Object.class,
      String.class,
      Integer.class,
      org.quackery.contract.collection.bug.alien.HasCollectionConstructors.class,
      org.quackery.contract.collection.bug.alien.FakeCollection.class);

  private static final List<Class<?>> collectionConstructorBugs = classes(
      org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorIsMissing.class,
      org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorIsHidden.class,
      org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorAddsElement.class,
      org.quackery.contract.collection.bug.collection.constructor.CopyConstructorIsMissing.class,
      org.quackery.contract.collection.bug.collection.constructor.CopyConstructorIsHidden.class);

  private static final List<Class<?>> collectionFactoryBugs = classes(
      org.quackery.contract.collection.bug.collection.factory.FactoryIsMissing.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryIsHidden.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryIsNotStatic.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryReturnsObject.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryHasDifferentName.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryAcceptsObject.class);

  private static final List<Class<?>> collectionBugs = classes(
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
      org.quackery.contract.collection.bug.collection.IteratorNextReturnsNullAfterTraversing.class);

  private static final List<Class<?>> collectionImmutableBugs = classes(
      org.quackery.contract.collection.bug.collection.immutable.IteratorRemovesElement.class,
      org.quackery.contract.collection.bug.collection.immutable.IteratorDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.AddAddsElement.class,
      org.quackery.contract.collection.bug.collection.immutable.AddDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveRemovesElement.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.AddAllAddsElements.class,
      org.quackery.contract.collection.bug.collection.immutable.AddAllDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveAllRemovesElements.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveAllDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.RetainAllRetainsElements.class,
      org.quackery.contract.collection.bug.collection.immutable.RetainAllDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.ClearClearsElements.class,
      org.quackery.contract.collection.bug.collection.immutable.ClearDoesNotThrowException.class);

  private static final List<Class<?>> collectionForbiddingNullBugs = classes(
      org.quackery.contract.collection.bug.collection.nulls.forbidding.CreatorAllowsNullElements.class);

  private static final List<Class<?>> collectionMutableForbiddingNullBugs = classes(
      org.quackery.contract.collection.bug.collection.nulls.forbidding.AddAllowsNullElements.class);

  private static final List<Class<?>> collectionAllowingNullBugs = classes(
      org.quackery.contract.collection.bug.collection.nulls.allowing.CreatorForbidsNullElements.class);

  private static final List<Class<?>> collectionMutableAllowingNullBugs = classes(
      org.quackery.contract.collection.bug.collection.nulls.allowing.AddForbidsNullElements.class);

  private static final List<Class<?>> listImmutableBugs = classes(
      org.quackery.contract.collection.bug.list.immutable.AddAllIntAddsElements.class,
      org.quackery.contract.collection.bug.list.immutable.AddAllIntDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.SetSetsElement.class,
      org.quackery.contract.collection.bug.list.immutable.SetDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.AddIntAddsElement.class,
      org.quackery.contract.collection.bug.list.immutable.AddIntDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.RemoveIntRemovesElement.class,
      org.quackery.contract.collection.bug.list.immutable.RemoveIntDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorRemovesElement.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorRemoveDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorSetsElement.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorSetDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorAddsElement.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorAddDoesNotThrowException.class);

  private static final List<Class<?>> listFactoryBugs = classes(
      org.quackery.contract.collection.bug.list.factory.FactoryReturnsCollection.class);

  private static final List<Class<?>> listBugs = classes(
      org.quackery.contract.collection.bug.list.CopyConstructorStoresOneElement.class,
      org.quackery.contract.collection.bug.list.CopyConstructorReversesOrder.class,
      org.quackery.contract.collection.bug.list.CopyConstructorRemovesLastElement.class,
      org.quackery.contract.collection.bug.list.CopyConstructorRemovesDuplicates.class,
      org.quackery.contract.collection.bug.list.GetReturnsFirstElement.class,
      org.quackery.contract.collection.bug.list.GetReturnsLastElement.class,
      org.quackery.contract.collection.bug.list.GetReturnsNull.class,
      org.quackery.contract.collection.bug.list.GetReturnsNullAboveBound.class,
      org.quackery.contract.collection.bug.list.GetReturnsNullBelowBound.class);

  private static final List<Class<?>> collectionMutableBugs = classes(
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesSwallowsException.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesThrowsException.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesThrowsInverted.class,
      org.quackery.contract.collection.bug.collection.mutable.IteratorRemovesIgnoresSecondCall.class,
      org.quackery.contract.collection.bug.collection.mutable.AddHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.AddReturnsNegation.class,
      org.quackery.contract.collection.bug.collection.mutable.AddAllHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.RemoveHasNoEffect.class,
      org.quackery.contract.collection.bug.collection.mutable.ClearHasNoEffect.class);

  private static final List<Class<?>> listMutableBugs = classes(
      org.quackery.contract.collection.bug.list.mutable.AddAddsAtTheBegin.class,
      org.quackery.contract.collection.bug.list.mutable.AddReturnsFalse.class,
      org.quackery.contract.collection.bug.list.mutable.AddNotAddsDuplicatedElement.class,
      org.quackery.contract.collection.bug.list.mutable.SetAddsElement.class,
      org.quackery.contract.collection.bug.list.mutable.SetReturnsInsertedElement.class,
      org.quackery.contract.collection.bug.list.mutable.SetIndexesFromEnd.class,
      org.quackery.contract.collection.bug.list.mutable.AddIntAddsAtTheEnd.class);

  private static List<Class<?>> classes(Class<?>... classes) {
    return unmodifiableList(Arrays.asList(classes));
  }
}
