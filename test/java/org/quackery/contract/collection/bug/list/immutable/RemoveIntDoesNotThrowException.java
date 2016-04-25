package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.ImmutableList;

public class RemoveIntDoesNotThrowException<E> extends ImmutableList<E> {
  public RemoveIntDoesNotThrowException() {}

  public RemoveIntDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public E remove(int index) {
    return null;
  }
}
