package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class IsEmptyReturnsFalse<E> extends MutableList<E> {
  public IsEmptyReturnsFalse() {}

  public IsEmptyReturnsFalse(Collection<E> collection) {
    super(collection);
  }

  public boolean isEmpty() {
    return false;
  }
}
