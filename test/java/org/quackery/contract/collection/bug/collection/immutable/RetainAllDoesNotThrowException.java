package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.ImmutableList;

public class RetainAllDoesNotThrowException<E> extends ImmutableList<E> {
  public RetainAllDoesNotThrowException() {}

  public RetainAllDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public boolean retainAll(Collection<?> c) {
    return false;
  }
}
