package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorHasNextNegates<E> extends MutableList<E> {
  public IteratorHasNextNegates() {}

  public IteratorHasNextNegates(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    final Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      public boolean hasNext() {
        return !iterator.hasNext();
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
