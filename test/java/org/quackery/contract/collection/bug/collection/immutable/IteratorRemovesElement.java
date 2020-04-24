package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.ImmutableList;

public class IteratorRemovesElement<E> extends ImmutableList<E> {
  public IteratorRemovesElement() {}

  public IteratorRemovesElement(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    Iterator<E> iterator = delegate.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return iterator.hasNext();
      }

      public E next() {
        return iterator.next();
      }

      public void remove() {
        iterator.remove();
        throw new UnsupportedOperationException();
      }
    };
  }
}
