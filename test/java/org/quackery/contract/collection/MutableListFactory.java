package org.quackery.contract.collection;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.Bug;

public class MutableListFactory {
  public static <E> List<E> create(Collection<? extends E> collection) {
    return new MutableList(collection);
  }

  // no constructors
  private MutableListFactory(Object o) {}

  @Bug(Collection.class)
  public static class FactoryIsMissing {}

  @Bug(Collection.class)
  public static class FactoryIsHidden {
    static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryIsNotStatic {
    public <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryReturnsObject {
    public static <E> Object create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryHasDifferentName {
    public static <E> List<E> differentName(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryAcceptsObject {
    public static <E> List<E> create(Object collection) {
      return new MutableList((Collection) collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryCreatesEmpty {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorCreatesEmpty(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryAddsElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorAddsElement(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryAcceptsNull {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorAcceptsNull(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryMakesNoDefensiveCopy {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorMakesNoDefensiveCopy(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryModifiesArgument {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorModifiesArgument(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryCreatesFixed {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorCreatesFixed(collection);
    }
  }

  @Bug(List.class)
  public static class FactoryStoresOneElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorStoresOneElement(collection);
    }
  }

  @Bug(List.class)
  public static class FactoryReversesOrder {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorReversesOrder(collection);
    }
  }

  @Bug(List.class)
  public static class FactoryRemovesLastElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.CopyConstructorRemovesLastElement(collection);
    }
  }

  @Bug(Collection.class)
  public static class ToArrayReturnsEmpty {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.ToArrayReturnsEmpty(collection);
    }
  }

  @Bug(Collection.class)
  public static class ToArrayReturnsUnknownElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.ToArrayReturnsUnknownElement(collection);
    }
  }

  @Bug(Collection.class)
  public static class ToArrayReturnsNull {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.ToArrayReturnsNull(collection);
    }
  }

  @Bug(Collection.class)
  public static class SizeReturnsZero {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.SizeReturnsZero(collection);
    }
  }

  @Bug(Collection.class)
  public static class SizeReturnsOne {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.SizeReturnsOne(collection);
    }
  }

  @Bug(Collection.class)
  public static class IsEmptyReturnsTrue {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IsEmptyReturnsTrue(collection);
    }
  }

  @Bug(Collection.class)
  public static class IsEmptyReturnsFalse {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IsEmptyReturnsFalse(collection);
    }
  }

  @Bug(Collection.class)
  public static class IsEmptyNegates {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IsEmptyNegates(collection);
    }
  }

  @Bug(Collection.class)
  public static class ContainsReturnsTrue {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.ContainsReturnsTrue(collection);
    }
  }

  @Bug(Collection.class)
  public static class ContainsReturnsFalse {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.ContainsReturnsFalse(collection);
    }
  }

  @Bug(Collection.class)
  public static class ContainsNegates {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.ContainsNegates(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorReturnsNull {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorReturnsNull(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorIsEmpty {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorIsEmpty(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorHasUnknownElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorHasUnknownElement(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorHasUnknownElementIfCollectionIsEmpty {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorHasUnknownElementIfCollectionIsEmpty(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorRepeatsFirstElementInfinitely {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorRepeatsFirstElementInfinitely(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorInsertsFirstElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorInsertsFirstElement(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorInsertsLastElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorInsertsLastElement(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorSkipsFirstElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorSkipsFirstElement(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorHasNextAlways {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorHasNextAlways(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorHasNextNever {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorHasNextNever(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorHasNextNegates {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorHasNextNegates(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorHasNextIfCollectionIsEmpty {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorHasNextIfCollectionIsEmpty(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorHasNextIfCollectionIsNotEmpty {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorHasNextIfCollectionIsNotEmpty(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorNextReturnsUnknownElementAfterTraversing {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorNextReturnsUnknownElementAfterTraversing(collection);
    }
  }

  @Bug(Collection.class)
  public static class IteratorNextReturnsNullAfterTraversing {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorNextReturnsNullAfterTraversing(collection);
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesHasNoEffect {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorRemovesHasNoEffect(collection);
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesSwallowsException {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorRemovesSwallowsException(collection);
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesThrowsException {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorRemovesThrowsException(collection);
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesThrowsInverted {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorRemovesThrowsInverted(collection);
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesIgnoresSecondCall {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.IteratorRemovesIgnoresSecondCall(collection);
    }
  }

  @Bug(List.class)
  public static class GetReturnsFirstElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.GetReturnsFirstElement(collection);
    }
  }

  @Bug(List.class)
  public static class GetReturnsLastElement {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.GetReturnsLastElement(collection);
    }
  }

  @Bug(List.class)
  public static class GetReturnsNull {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.GetReturnsNull(collection);
    }
  }

  @Bug(List.class)
  public static class GetReturnsNullAboveBound {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.GetReturnsNullAboveBound(collection);
    }
  }

  @Bug(List.class)
  public static class GetReturnsNullBelowBound {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.GetReturnsNullBelowBound(collection);
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class ClearHasNoEffect {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.ClearHasNoEffect(collection);
    }
  }

  @Bug({ List.class, Mutable.class })
  public static class AddHasNoEffect {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.AddHasNoEffect(collection);
    }
  }

  @Bug({ List.class, Mutable.class })
  public static class AddAddsAtTheBegin {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.AddAddsAtTheBegin(collection);
    }
  }

  @Bug({ List.class, Mutable.class })
  public static class AddReturnsFalse {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.AddReturnsFalse(collection);
    }
  }
}
