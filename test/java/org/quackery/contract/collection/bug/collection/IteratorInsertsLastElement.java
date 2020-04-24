package org.quackery.contract.collection.bug.collection;

import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorInsertsLastElement<E> extends MutableList<E> {
  public IteratorInsertsLastElement() {}

  public IteratorInsertsLastElement(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    Iterator<E> iterator = super.iterator();
    return new Iterator<E>() {
      private boolean usedExtra = false;

      public boolean hasNext() {
        return iterator.hasNext() || !usedExtra;
      }

      public E next() {
        if (iterator.hasNext()) {
          return iterator.next();
        } else if (!usedExtra) {
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
