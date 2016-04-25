package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class RemoveHasNoEffect<E> extends MutableList<E> {
  public RemoveHasNoEffect() {}

  public RemoveHasNoEffect(Collection<E> collection) {
    super(collection);
  }

  public boolean remove(Object o) {
    return true;
  }
}
