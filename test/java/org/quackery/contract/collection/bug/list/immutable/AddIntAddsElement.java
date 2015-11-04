package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class AddIntAddsElement<E> extends ImmutableList<E> {
  public AddIntAddsElement() {}

  public AddIntAddsElement(Collection<E> collection) {
    super(collection);
  }

  public void add(int index, E element) {
    delegate.add(index, element);
    throw new UnsupportedOperationException();
  }
}
