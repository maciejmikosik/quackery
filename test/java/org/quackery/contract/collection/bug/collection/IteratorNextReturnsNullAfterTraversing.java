package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorNextReturnsNullAfterTraversing<E> extends MutableList<E> {
  public IteratorNextReturnsNullAfterTraversing() {}

  public IteratorNextReturnsNullAfterTraversing(Collection<E> collection) {
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
            : null;
      }

      public void remove() {
        iterator.remove();
      }
    };
  }
}
