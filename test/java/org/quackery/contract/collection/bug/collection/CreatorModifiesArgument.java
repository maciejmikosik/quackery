package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class CreatorModifiesArgument<E> extends MutableList<E> {
  public CreatorModifiesArgument() {}

  public CreatorModifiesArgument(Collection<E> collection) {
    super(collection);
    collection.add((E) "x");
  }
}
