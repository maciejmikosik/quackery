package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;

public class test_mutable_list_contract {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(List.class)
        .mutable();

    for (Class<?> bug : asList(
        AddHasNoEffect.class,
        AddAddsAtTheBegin.class,
        AddReturnsFalse.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.test(asListFactory(bug)));
    }
  }

  public static class AddHasNoEffect<E> extends MutableList<E> {
    public AddHasNoEffect() {}

    public AddHasNoEffect(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      return true;
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
}
