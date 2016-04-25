package org.quackery.contract.collection.bug.list.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class SetReturnsInsertedElement<E> extends MutableList<E> {
  public SetReturnsInsertedElement() {}

  public SetReturnsInsertedElement(Collection<E> collection) {
    super(collection);
  }

  public E set(int index, E element) {
    super.set(index, element);
    return element;
  }
}
