package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class ContainsReturnsFalse<E> extends MutableList<E> {
  public ContainsReturnsFalse() {}

  public ContainsReturnsFalse(Collection<E> collection) {
    super(collection);
  }

  public boolean contains(Object o) {
    return false;
  }
}
