package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.ImmutableList;

public class AddAddsElement<E> extends ImmutableList<E> {
  public AddAddsElement() {}

  public AddAddsElement(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    delegate.add(e);
    throw new UnsupportedOperationException();
  }
}
