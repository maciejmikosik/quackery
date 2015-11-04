package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorRepeatsFirstElementInfinitely<E> extends MutableList<E> {
  public IteratorRepeatsFirstElementInfinitely() {}

  public IteratorRepeatsFirstElementInfinitely(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    return new Iterator<E>() {
      public boolean hasNext() {
        return delegate.iterator().hasNext();
      }

      public E next() {
        return delegate.iterator().next();
      }

      public void remove() {
        delegate.iterator().remove();
      }
    };
  }
}
