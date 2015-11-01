package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorRemovesSwallowsException<E> extends MutableList<E> {
  public IteratorRemovesSwallowsException() {}

  public IteratorRemovesSwallowsException(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    final Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return iterator.hasNext();
      }

      public E next() {
        return iterator.next();
      }

      public void remove() {
        try {
          iterator.remove();
        } catch (IllegalStateException e) {}
      }
    };
  }
}
