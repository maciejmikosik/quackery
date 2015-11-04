package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class ToArrayReturnsNull<E> extends MutableList<E> {
  public ToArrayReturnsNull() {}

  public ToArrayReturnsNull(Collection<E> collection) {
    super(collection);
  }

  public Object[] toArray() {
    return null;
  }
}
