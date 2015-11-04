package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.MutableList;

public class CreatorMakesNoDefensiveCopy<E> extends MutableList<E> {
  public CreatorMakesNoDefensiveCopy() {}

  public CreatorMakesNoDefensiveCopy(Collection<E> collection) {
    super(collection);
    delegate = (List<E>) collection;
  }
}
