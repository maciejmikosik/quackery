package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorHasNextIfCollectionIsEmpty<E> extends MutableList<E> {
  public IteratorHasNextIfCollectionIsEmpty() {}

  public IteratorHasNextIfCollectionIsEmpty(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    final Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return isEmpty();
      }

      public E next() {
        return iterator.next();
      }

      public void remove() {
        iterator.remove();
      }
    };
  }
}
