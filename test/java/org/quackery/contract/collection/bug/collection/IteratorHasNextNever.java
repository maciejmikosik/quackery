package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorHasNextNever<E> extends MutableList<E> {
  public IteratorHasNextNever() {}

  public IteratorHasNextNever(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return false;
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
