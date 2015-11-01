package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class AddIntDoesNotThrowException<E> extends ImmutableList<E> {
  public AddIntDoesNotThrowException() {}

  public AddIntDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public void add(int index, E element) {}
}
