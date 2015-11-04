package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorInsertsFirstElement<E> extends MutableList<E> {
  public IteratorInsertsFirstElement() {}

  public IteratorInsertsFirstElement(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    final Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      private boolean usedExtra = false;

      public boolean hasNext() {
        return !usedExtra || iterator.hasNext();
      }

      public E next() {
        if (!usedExtra) {
          usedExtra = true;
          return (E) "x";
        } else {
          return iterator.next();
        }
      }

      public void remove() {
        iterator.remove();
      }
    };
  }
}
