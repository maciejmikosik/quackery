package org.quackery.contract.collection.bug.list.factory;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class FactoryReturnsCollection {
  public static <E> Collection<E> create(Collection<? extends E> collection) {
    return new MutableList(collection);
  }
}
