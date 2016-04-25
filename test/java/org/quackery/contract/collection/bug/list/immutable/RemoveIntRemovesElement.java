package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.ImmutableList;

public class RemoveIntRemovesElement<E> extends ImmutableList<E> {
  public RemoveIntRemovesElement() {}

  public RemoveIntRemovesElement(Collection<E> collection) {
    super(collection);
  }

  public E remove(int index) {
    delegate.remove(index);
    throw new UnsupportedOperationException();
  }
}
