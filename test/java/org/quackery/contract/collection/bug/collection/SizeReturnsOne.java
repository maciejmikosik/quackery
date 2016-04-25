package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class SizeReturnsOne<E> extends MutableList<E> {
  public SizeReturnsOne() {}

  public SizeReturnsOne(Collection<E> collection) {
    super(collection);
  }

  public int size() {
    return 1;
  }
}
