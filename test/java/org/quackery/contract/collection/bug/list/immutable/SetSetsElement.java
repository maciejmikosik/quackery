package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class SetSetsElement<E> extends ImmutableList<E> {
  public SetSetsElement() {}

  public SetSetsElement(Collection<E> collection) {
    super(collection);
  }

  public E set(int index, E element) {
    delegate.set(index, element);
    throw new UnsupportedOperationException();
  }
}
