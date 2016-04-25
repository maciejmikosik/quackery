package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class IsEmptyReturnsTrue<E> extends MutableList<E> {
  public IsEmptyReturnsTrue() {}

  public IsEmptyReturnsTrue(Collection<E> collection) {
    super(collection);
  }

  public boolean isEmpty() {
    return true;
  }
}
