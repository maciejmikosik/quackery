package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class ToArrayReturnsEmpty<E> extends MutableList<E> {
  public ToArrayReturnsEmpty() {}

  public ToArrayReturnsEmpty(Collection<E> collection) {
    super(collection);
  }

  public Object[] toArray() {
    return new Object[0];
  }
}
