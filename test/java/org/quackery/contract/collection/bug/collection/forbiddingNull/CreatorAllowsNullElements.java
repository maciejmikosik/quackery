package org.quackery.contract.collection.bug.collection.forbiddingNull;

import java.util.ArrayList;
import java.util.Collection;

import org.quackery.contract.collection.correct.MutableListForbiddingNullNicely;

public class CreatorAllowsNullElements<E> extends MutableListForbiddingNullNicely<E> {
  public CreatorAllowsNullElements() {}

  public CreatorAllowsNullElements(Collection<E> collection) {
    delegate = new ArrayList<>(collection);
  }
}
