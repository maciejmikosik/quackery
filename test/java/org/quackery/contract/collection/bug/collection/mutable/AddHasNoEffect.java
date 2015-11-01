package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class AddHasNoEffect<E> extends MutableList<E> {
  public AddHasNoEffect() {}

  public AddHasNoEffect(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    return true;
  }
}
