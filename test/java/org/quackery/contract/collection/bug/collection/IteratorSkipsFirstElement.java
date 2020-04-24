package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorSkipsFirstElement<E> extends MutableList<E> {
  public IteratorSkipsFirstElement() {}

  public IteratorSkipsFirstElement(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    Iterator<E> iterator = super.iterator();
    if (iterator.hasNext()) {
      iterator.next();
    }
    return iterator;
  }
}
