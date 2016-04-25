package org.quackery.contract.collection.bug.list.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class AddNotAddsDuplicatedElement<E> extends MutableList<E> {
  public AddNotAddsDuplicatedElement() {}

  public AddNotAddsDuplicatedElement(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    if (!contains(e)) {
      super.add(e);
    }
    return true;
  }
}
