package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorRemovesIgnoresSecondCall<E> extends MutableList<E> {
  public IteratorRemovesIgnoresSecondCall() {}

  public IteratorRemovesIgnoresSecondCall(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    final Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      boolean removed = false;

      public boolean hasNext() {
        return iterator.hasNext();
      }

      public E next() {
        removed = false;
        return iterator.next();
      }

      public void remove() {
        if (!removed) {
          iterator.remove();
          removed = true;
        }
      }
    };
  }
}
