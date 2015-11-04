package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class ContainsReturnsTrue<E> extends MutableList<E> {
  public ContainsReturnsTrue() {}

  public ContainsReturnsTrue(Collection<E> collection) {
    super(collection);
  }

  public boolean contains(Object o) {
    return true;
  }
}
