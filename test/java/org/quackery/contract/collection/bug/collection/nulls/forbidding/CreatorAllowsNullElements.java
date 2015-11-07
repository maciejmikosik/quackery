package org.quackery.contract.collection.bug.collection.nulls.forbidding;

import java.util.ArrayList;
import java.util.Collection;

import org.quackery.contract.collection.MutableListForbiddingNullNicely;

public class CreatorAllowsNullElements<E> extends MutableListForbiddingNullNicely<E> {
  public CreatorAllowsNullElements() {}

  public CreatorAllowsNullElements(Collection<E> collection) {
    delegate = new ArrayList<>(collection);
  }
}
