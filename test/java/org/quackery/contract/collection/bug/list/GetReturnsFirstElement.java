package org.quackery.contract.collection.bug.list;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class GetReturnsFirstElement<E> extends MutableList<E> {
  public GetReturnsFirstElement() {}

  public GetReturnsFirstElement(Collection<E> collection) {
    super(collection);
  }

  public E get(int index) {
    return super.get(0);
  }
}
