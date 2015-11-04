package org.quackery.contract.collection.bug.collection.immutable;

import java.util.Collection;

import org.quackery.contract.collection.ImmutableList;

public class RetainAllRetainsElements<E> extends ImmutableList<E> {
  public RetainAllRetainsElements() {}

  public RetainAllRetainsElements(Collection<E> collection) {
    super(collection);
  }

  public boolean retainAll(Collection<?> c) {
    delegate.retainAll(c);
    throw new UnsupportedOperationException();
  }
}
