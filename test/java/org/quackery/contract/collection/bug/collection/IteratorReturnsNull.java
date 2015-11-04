package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorReturnsNull<E> extends MutableList<E> {
  public IteratorReturnsNull() {}

  public IteratorReturnsNull(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    return null;
  }
}
