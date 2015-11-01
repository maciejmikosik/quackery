package org.quackery.contract.collection.bug.list;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class GetReturnsNullBelowBound<E> extends MutableList<E> {
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
