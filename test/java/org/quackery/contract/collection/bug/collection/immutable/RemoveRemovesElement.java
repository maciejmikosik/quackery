package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class RemoveRemovesElement<E> extends ImmutableList<E> {
  public RemoveRemovesElement() {}

  public RemoveRemovesElement(Collection<E> collection) {
    super(collection);
  }

  public boolean remove(Object o) {
    delegate.remove(o);
    throw new UnsupportedOperationException();
  }
}
