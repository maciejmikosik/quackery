package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

public class test_mutable_list_contract {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .mutable();
    for (Class<?> bug : asList(
        AddAddsAtTheBegin.class,
        AddReturnsFalse.class,
        AddNotAddsDuplicatedElement.class)) {
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
}
