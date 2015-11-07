package org.quackery.contract.collection.bug.collection.nulls.allowing;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

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
