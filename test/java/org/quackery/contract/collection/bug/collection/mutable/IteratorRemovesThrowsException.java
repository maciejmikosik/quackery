package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorRemovesThrowsException<E> extends MutableList<E> {
  public IteratorRemovesThrowsException() {}

  public IteratorRemovesThrowsException(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return iterator.hasNext();
      }

      public E next() {
        return iterator.next();
      }

      public void remove() {
        throw new IllegalStateException();
      }
    };
  }
}
