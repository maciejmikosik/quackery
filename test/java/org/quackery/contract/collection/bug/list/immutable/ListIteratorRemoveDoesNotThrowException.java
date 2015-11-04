package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;
import java.util.ListIterator;

import org.quackery.contract.collection.ImmutableList;

public class ListIteratorRemoveDoesNotThrowException<E> extends ImmutableList<E> {
  public ListIteratorRemoveDoesNotThrowException() {}

  public ListIteratorRemoveDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public ListIterator<E> listIterator() {
    return new UnmodifiableIterator(delegate.listIterator()) {
      public void remove() {}
    };
  }
}
