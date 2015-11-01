package org.quackery.contract.collection.bug.list.mutable;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class AddReturnsFalse<E> extends MutableList<E> {
  public AddReturnsFalse() {}

  public AddReturnsFalse(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    super.add(e);
    return false;
  }
}
