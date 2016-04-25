package org.quackery.contract.collection.bug.collection;

import static java.util.Arrays.asList;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class CreatorCreatesFixed<E> extends MutableList<E> {
  public CreatorCreatesFixed() {}

  public CreatorCreatesFixed(Collection<E> collection) {
    super(asList((E) "x"));
    if (collection == null) {
      throw new NullPointerException();
    }
  }
}
