package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.ImmutableList;

public class ClearDoesNotThrowException<E> extends ImmutableList<E> {
  public ClearDoesNotThrowException() {}

  public ClearDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public void clear() {}
}
