package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class SizeReturnsZero<E> extends MutableList<E> {
  public SizeReturnsZero() {}

  public SizeReturnsZero(Collection<E> collection) {
    super(collection);
  }

  public int size() {
    return 0;
  }
}
