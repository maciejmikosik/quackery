package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.ImmutableList;

public class RemoveAllDoesNotThrowException<E> extends ImmutableList<E> {
  public RemoveAllDoesNotThrowException() {}

  public RemoveAllDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public boolean removeAll(Collection<?> c) {
    return false;
  }
}
