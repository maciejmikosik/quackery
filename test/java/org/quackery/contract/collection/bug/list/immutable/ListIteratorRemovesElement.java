package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;
import java.util.ListIterator;

import org.quackery.contract.collection.correct.ImmutableList;

public class ListIteratorRemovesElement<E> extends ImmutableList<E> {
  public ListIteratorRemovesElement() {}

  public ListIteratorRemovesElement(Collection<E> collection) {
    super(collection);
  }

  public ListIterator<E> listIterator() {
    return new UnmodifiableIterator(delegate.listIterator()) {
      public void remove() {
        delegateIterator.remove();
        throw new UnsupportedOperationException();
      }
    };
  }
}
