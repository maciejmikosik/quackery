package org.quackery.contract.collection.bug.list;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class GetReturnsNull<E> extends MutableList<E> {
  public GetReturnsNull() {}

  public GetReturnsNull(Collection<E> collection) {
    super(collection);
  }

  public E get(int index) {
    return null;
  }
}
