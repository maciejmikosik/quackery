package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.ImmutableList;

public class RemoveDoesNotThrowException<E> extends ImmutableList<E> {
  public RemoveDoesNotThrowException() {}

  public RemoveDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public boolean remove(Object o) {
    return false;
  }
}
