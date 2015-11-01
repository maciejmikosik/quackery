package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorNextReturnsUnknownElementAfterTraversing<E> extends MutableList<E> {
  public IteratorNextReturnsUnknownElementAfterTraversing() {}

  public IteratorNextReturnsUnknownElementAfterTraversing(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    final Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return iterator.hasNext();
      }

      public E next() {
        return iterator.hasNext()
            ? iterator.next()
            : (E) "x";
      }

      public void remove() {
        iterator.remove();
      }
    };
  }
}
