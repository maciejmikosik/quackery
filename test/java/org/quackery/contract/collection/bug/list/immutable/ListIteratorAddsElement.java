package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;
import java.util.ListIterator;

import org.quackery.contract.collection.ImmutableList;

public class ListIteratorAddsElement<E> extends ImmutableList<E> {
  public ListIteratorAddsElement() {}

  public ListIteratorAddsElement(Collection<E> collection) {
    super(collection);
  }

  public ListIterator<E> listIterator() {
    return new UnmodifiableIterator(delegate.listIterator()) {
      public void add(Object e) {
        delegateIterator.add(e);
        throw new UnsupportedOperationException();
      }
    };
  }
}
