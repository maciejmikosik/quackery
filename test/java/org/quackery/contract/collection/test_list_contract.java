package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class test_list_contract {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(List.class);

    for (Class<?> bug : asList(
        CopyConstructorStoresOneElement.class,
        CopyConstructorReversesOrder.class,
        CopyConstructorRemovesLastElement.class,
        GetReturnsFirstElement.class,
        GetReturnsLastElement.class,
        GetReturnsNull.class,
        GetReturnsNullAboveBound.class,
        GetReturnsNullBelowBound.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.test(asFactory(bug)));
    }
  }

  public static class CopyConstructorStoresOneElement<E> extends MutableList<E> {
    public CopyConstructorStoresOneElement() {}

    public CopyConstructorStoresOneElement(Collection<E> collection) {
      super(one(collection));
    }

    private static <E> Collection<E> one(Collection<E> collection) {
      return collection.isEmpty()
          ? collection
          : asList(collection.iterator().next());
    }
  }

  public static class CopyConstructorReversesOrder<E> extends MutableList<E> {
    public CopyConstructorReversesOrder() {}

    public CopyConstructorReversesOrder(Collection<E> collection) {
      super(reverse(collection));
    }

    private static <E> Collection<E> reverse(Collection<E> collection) {
      List<E> list = new ArrayList<>(collection);
      Collections.reverse(list);
      return list;
    }
  }

  public static class CopyConstructorRemovesLastElement<E> extends MutableList<E> {
    public CopyConstructorRemovesLastElement() {}

    public CopyConstructorRemovesLastElement(Collection<E> collection) {
      super(withoutLast(collection));
    }

    private static <E> Collection<E> withoutLast(Collection<E> collection) {
      List<E> list = new ArrayList<>(collection);
      if (!list.isEmpty()) {
        list.remove(list.size() - 1);
      }
      return list;
    }
  }

  public static class GetReturnsFirstElement<E> extends MutableList<E> {
    public GetReturnsFirstElement() {}

    public GetReturnsFirstElement(Collection<E> collection) {
      super(collection);
    }

    public E get(int index) {
      return super.get(0);
    }
  }

  public static class GetReturnsLastElement<E> extends MutableList<E> {
    public GetReturnsLastElement() {}

    public GetReturnsLastElement(Collection<E> collection) {
      super(collection);
    }

    public E get(int index) {
      return super.get(size() - 1);
    }
  }

  public static class GetReturnsNull<E> extends MutableList<E> {
    public GetReturnsNull() {}

    public GetReturnsNull(Collection<E> collection) {
      super(collection);
    }

    public E get(int index) {
      return null;
    }
  }

  public static class GetReturnsNullAboveBound<E> extends MutableList<E> {
    public GetReturnsNullAboveBound() {}

    public GetReturnsNullAboveBound(Collection<E> collection) {
      super(collection);
    }

    public E get(int index) {
      return index >= size()
          ? null
          : super.get(index);
    }
  }

  public static class GetReturnsNullBelowBound<E> extends MutableList<E> {
    public GetReturnsNullBelowBound() {}

    public GetReturnsNullBelowBound(Collection<E> collection) {
      super(collection);
    }

    public E get(int index) {
      return index < 0
          ? null
          : super.get(index);
    }
  }
}
