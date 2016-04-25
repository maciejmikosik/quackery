package org.quackery.contract.collection.bug.list.mutable;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class SetIndexesFromEnd<E> extends MutableList<E> {
  public SetIndexesFromEnd() {}

  public SetIndexesFromEnd(Collection<E> collection) {
    super(collection);
  }

  public E set(int index, E element) {
    return super.set(size() - 1 - index, element);
  }
}
