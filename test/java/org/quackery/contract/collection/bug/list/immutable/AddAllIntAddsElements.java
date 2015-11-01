package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class AddAllIntAddsElements<E> extends ImmutableList<E> {
  public AddAllIntAddsElements() {}

  public AddAllIntAddsElements(Collection<E> collection) {
    super(collection);
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    delegate.addAll(index, c);
    throw new UnsupportedOperationException();
  }
}
