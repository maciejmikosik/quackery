package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

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

  public static class AddAddsAtTheBegin<E> extends MutableList<E> {
    public AddAddsAtTheBegin() {}

    public AddAddsAtTheBegin(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      super.add(0, e);
      return true;
    }
  }

  public static class AddReturnsFalse<E> extends MutableList<E> {
    public AddReturnsFalse() {}

    public AddReturnsFalse(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      super.add(e);
      return false;
    }
  }

  public static class AddNotAddsDuplicatedElement<E> extends MutableList<E> {
    public AddNotAddsDuplicatedElement() {}

    public AddNotAddsDuplicatedElement(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      if (!contains(e)) {
        super.add(e);
      }
      return true;
    }
  }

  public static class SetAddsElement<E> extends MutableList<E> {
    public SetAddsElement() {}

    public SetAddsElement(Collection<E> collection) {
      super(collection);
    }

    public E set(int index, E element) {
      add(element);
      return element;
    }
  }

  public static class SetReturnsInsertedElement<E> extends MutableList<E> {
    public SetReturnsInsertedElement() {}

    public SetReturnsInsertedElement(Collection<E> collection) {
      super(collection);
    }

    public E set(int index, E element) {
      super.set(index, element);
      return element;
    }
  }

  public static class SetIndexesFromEnd<E> extends MutableList<E> {
    public SetIndexesFromEnd() {}

    public SetIndexesFromEnd(Collection<E> collection) {
      super(collection);
    }

    public E set(int index, E element) {
      return super.set(size() - 1 - index, element);
    }
  }

  public static class AddIntAddsAtTheEnd<E> extends MutableList<E> {
    public AddIntAddsAtTheEnd() {}

    public AddIntAddsAtTheEnd(Collection<E> collection) {
      super(collection);
    }

    public void add(int index, E element) {
      super.add(element);
    }
  }
}
