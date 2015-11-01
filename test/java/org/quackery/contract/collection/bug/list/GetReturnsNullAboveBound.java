package org.quackery.contract.collection.bug.list;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class GetReturnsNullAboveBound<E> extends MutableList<E> {
  public GetReturnsNullAboveBound() {}

  public GetReturnsNullAboveBound(Collection<E> collection) {
    super(collection);
  }

  public E get(int index) {
    return index >= size()
        ? null
        : super.get(index);
  }
}
