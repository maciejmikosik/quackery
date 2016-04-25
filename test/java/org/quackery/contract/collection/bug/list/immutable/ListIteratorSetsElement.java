package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;
import java.util.ListIterator;

import org.quackery.contract.collection.correct.ImmutableList;

public class ListIteratorSetsElement<E> extends ImmutableList<E> {
  public ListIteratorSetsElement() {}

  public ListIteratorSetsElement(Collection<E> collection) {
    super(collection);
  }

  public ListIterator<E> listIterator() {
    return new UnmodifiableIterator(delegate.listIterator()) {
      public void set(Object e) {
        delegateIterator.set(e);
        throw new UnsupportedOperationException();
      }
    };
  }
}
