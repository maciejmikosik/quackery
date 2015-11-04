package org.quackery.contract.collection.bug.alien;

import java.util.Collection;
import java.util.Iterator;

public class FakeCollection implements Collection {
  public FakeCollection() {}

  public FakeCollection(Collection<?> collection) {
    if (collection == null) {
      throw new NullPointerException();
    }
  }

  public int size() {
    return 0;
  }

  public boolean isEmpty() {
    return false;
  }

  public boolean contains(Object o) {
    return false;
  }

  public Iterator iterator() {
    return null;
  }

  public Object[] toArray() {
    return null;
  }

  public Object[] toArray(Object[] a) {
    return null;
  }

  public boolean add(Object e) {
    return false;
  }

  public boolean remove(Object o) {
    return false;
  }

  public boolean containsAll(Collection c) {
    return false;
  }

  public boolean addAll(Collection c) {
    return false;
  }

  public boolean removeAll(Collection c) {
    return false;
  }

  public boolean retainAll(Collection c) {
    return false;
  }

  public void clear() {}
}
