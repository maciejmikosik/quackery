package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class ClearHasNoEffect<E> extends MutableList<E> {
  public ClearHasNoEffect() {}

  public ClearHasNoEffect(Collection<E> collection) {
    super(collection);
  }

  public void clear() {}
}
