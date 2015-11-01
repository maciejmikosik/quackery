package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorHasNextAlways<E> extends MutableList<E> {
  public IteratorHasNextAlways() {}

  public IteratorHasNextAlways(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    final Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return true;
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
