package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class AddAllAddsElements<E> extends ImmutableList<E> {
  public AddAllAddsElements() {}

  public AddAllAddsElements(Collection<E> collection) {
    super(collection);
  }

  public boolean addAll(Collection<? extends E> c) {
    delegate.addAll(c);
    throw new UnsupportedOperationException();
  }
}
