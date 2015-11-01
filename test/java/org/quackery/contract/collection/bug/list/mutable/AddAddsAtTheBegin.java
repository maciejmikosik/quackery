package org.quackery.contract.collection.bug.list.mutable;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class AddAddsAtTheBegin<E> extends MutableList<E> {
  public AddAddsAtTheBegin() {}

  public AddAddsAtTheBegin(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    super.add(0, e);
    return true;
  }
}
