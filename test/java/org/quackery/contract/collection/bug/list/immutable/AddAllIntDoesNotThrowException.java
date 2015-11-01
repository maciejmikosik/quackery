package org.quackery.contract.collection.bug.list.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class AddAllIntDoesNotThrowException<E> extends ImmutableList<E> {
  public AddAllIntDoesNotThrowException() {}

  public AddAllIntDoesNotThrowException(Collection<E> collection) {
    super(collection);
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    return false;
  }
}
