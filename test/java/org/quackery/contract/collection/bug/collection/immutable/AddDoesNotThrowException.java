package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class AddDoesNotThrowException<E> extends ImmutableList<E> {
  public AddDoesNotThrowException() {}

  public AddDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    return false;
  }
}
