package org.quackery.contract.collection.bug.list;

import static java.util.Arrays.asList;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class CopyConstructorStoresOneElement<E> extends MutableList<E> {
  public CopyConstructorStoresOneElement() {}

  public CopyConstructorStoresOneElement(Collection<E> collection) {
    super(one(collection));
  }

  private static <E> Collection<E> one(Collection<E> collection) {
    return collection.isEmpty()
        ? collection
        : asList(collection.iterator().next());
  }
}
