package org.quackery.contract.collection.bug.list.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class SetAddsElement<E> extends MutableList<E> {
  public SetAddsElement() {}

  public SetAddsElement(Collection<E> collection) {
    super(collection);
  }

  public E set(int index, E element) {
    add(element);
    return element;
  }
}
