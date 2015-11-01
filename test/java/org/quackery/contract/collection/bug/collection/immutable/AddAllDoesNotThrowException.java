package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class AddAllDoesNotThrowException<E> extends ImmutableList<E> {
  public AddAllDoesNotThrowException() {}

  public AddAllDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public boolean addAll(Collection<? extends E> c) {
    return false;
  }
}
