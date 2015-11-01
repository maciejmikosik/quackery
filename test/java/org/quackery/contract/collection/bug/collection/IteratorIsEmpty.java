package org.quackery.contract.collection.bug.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.MutableList;

public class IteratorIsEmpty<E> extends MutableList<E> {
  public IteratorIsEmpty() {}

  public IteratorIsEmpty(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    return new ArrayList<E>().iterator();
  }
}
