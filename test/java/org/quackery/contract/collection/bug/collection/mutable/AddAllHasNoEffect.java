package org.quackery.contract.collection.bug.collection.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class AddAllHasNoEffect<E> extends MutableList<E> {
  public AddAllHasNoEffect() {}

  public AddAllHasNoEffect(Collection<E> collection) {
    super(collection);
  }

  public boolean addAll(Collection<? extends E> collection) {
    return true;
  }
}
