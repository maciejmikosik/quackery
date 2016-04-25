package org.quackery.contract.collection.bug.collection.mutableAllowingNull;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class AddForbidsNullElements<E> extends MutableList<E> {
  public AddForbidsNullElements() {}

  public AddForbidsNullElements(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    if (e == null) {
      throw new NullPointerException();
    }
    return super.add(e);
  }
}
