package org.quackery.contract.collection.bug.list.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class AddIntAddsAtTheEnd<E> extends MutableList<E> {
  public AddIntAddsAtTheEnd() {}

  public AddIntAddsAtTheEnd(Collection<E> collection) {
    super(collection);
  }

  public void add(int index, E element) {
    super.add(element);
  }
}
