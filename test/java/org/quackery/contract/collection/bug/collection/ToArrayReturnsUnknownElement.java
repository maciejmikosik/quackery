package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class ToArrayReturnsUnknownElement<E> extends MutableList<E> {
  public ToArrayReturnsUnknownElement() {}

  public ToArrayReturnsUnknownElement(Collection<E> collection) {
    super(collection);
  }

  public Object[] toArray() {
    return new Object[] { "x" };
  }
}
