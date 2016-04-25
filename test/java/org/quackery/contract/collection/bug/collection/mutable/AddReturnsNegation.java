package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class AddReturnsNegation<E> extends MutableList<E> {
  public AddReturnsNegation() {}

  public AddReturnsNegation(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    return !super.add(e);
  }
}
