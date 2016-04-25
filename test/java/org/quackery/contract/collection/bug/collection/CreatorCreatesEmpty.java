package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class CreatorCreatesEmpty<E> extends MutableList<E> {
  public CreatorCreatesEmpty() {}

  public CreatorCreatesEmpty(Collection<E> collection) {
    if (collection == null) {
      throw new NullPointerException();
    }
  }
}
