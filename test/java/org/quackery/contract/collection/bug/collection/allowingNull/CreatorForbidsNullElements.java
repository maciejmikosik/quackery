package org.quackery.contract.collection.bug.collection.allowingNull;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class CreatorForbidsNullElements<E> extends MutableList<E> {
  public CreatorForbidsNullElements() {}

  public CreatorForbidsNullElements(Collection<E> collection) {
    super(collection);
    for (E element : delegate) {
      if (element == null) {
        throw new NullPointerException();
      }
    }
  }
}
