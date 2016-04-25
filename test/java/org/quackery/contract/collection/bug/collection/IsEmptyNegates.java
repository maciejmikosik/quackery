package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class IsEmptyNegates<E> extends MutableList<E> {
  public IsEmptyNegates() {}

  public IsEmptyNegates(Collection<E> collection) {
    super(collection);
  }

  public boolean isEmpty() {
    return !super.isEmpty();
  }
}
