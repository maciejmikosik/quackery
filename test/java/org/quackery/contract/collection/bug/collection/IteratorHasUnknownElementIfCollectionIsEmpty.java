package org.quackery.contract.collection.bug.collection;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.quackery.contract.collection.correct.MutableList;

public class IteratorHasUnknownElementIfCollectionIsEmpty<E> extends MutableList<E> {
  public IteratorHasUnknownElementIfCollectionIsEmpty() {}

  public IteratorHasUnknownElementIfCollectionIsEmpty(Collection<E> collection) {
    super(collection);
  }

  public Iterator<E> iterator() {
    return isEmpty()
        ? new ArrayList<E>(asList((E) "x")).iterator()
        : super.iterator();
  }
}
