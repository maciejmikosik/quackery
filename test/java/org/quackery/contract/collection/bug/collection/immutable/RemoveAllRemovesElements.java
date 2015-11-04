package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class RemoveAllRemovesElements<E> extends ImmutableList<E> {
  public RemoveAllRemovesElements() {}

  public RemoveAllRemovesElements(Collection<E> collection) {
    super(collection);
  }

  public boolean removeAll(Collection<?> c) {
    delegate.removeAll(c);
    throw new UnsupportedOperationException();
  }
}
