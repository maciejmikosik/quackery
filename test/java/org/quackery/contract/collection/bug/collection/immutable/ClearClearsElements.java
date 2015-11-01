package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class ClearClearsElements<E> extends ImmutableList<E> {
  public ClearClearsElements() {}

  public ClearClearsElements(Collection<E> collection) {
    super(collection);
  }

  public void clear() {
    delegate.clear();
    throw new UnsupportedOperationException();
  }
}
