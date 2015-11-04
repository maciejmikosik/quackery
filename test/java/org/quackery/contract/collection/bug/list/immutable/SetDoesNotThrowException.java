package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class SetDoesNotThrowException<E> extends ImmutableList<E> {
  public SetDoesNotThrowException() {}

  public SetDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public E set(int index, E element) {
    return null;
  }
}
