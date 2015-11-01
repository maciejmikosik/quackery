package org.quackery.contract.collection.bug.list;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class GetReturnsLastElement<E> extends MutableList<E> {
  public GetReturnsLastElement() {}

  public GetReturnsLastElement(Collection<E> collection) {
    super(collection);
  }

  public E get(int index) {
    return super.get(size() - 1);
  }
}
