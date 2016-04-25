package org.quackery.contract.collection.bug.collection;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorHasUnknownElement<E> extends MutableList<E> {
  public IteratorHasUnknownElement() {}

  public IteratorHasUnknownElement(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    return new ArrayList<E>(asList((E) "x")).iterator();
  }
}
