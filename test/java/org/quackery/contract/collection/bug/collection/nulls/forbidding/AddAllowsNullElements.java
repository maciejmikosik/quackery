package org.quackery.contract.collection.bug.collection.nulls.forbidding;

import java.util.Collection;

import org.quackery.contract.collection.MutableListForbiddingNullNicely;

public class AddAllowsNullElements<E> extends MutableListForbiddingNullNicely<E> {
  public AddAllowsNullElements() {}

  public AddAllowsNullElements(Collection<E> collection) {
    super(collection);
  }

  public boolean add(E e) {
    return delegate.add(e);
  }
}
