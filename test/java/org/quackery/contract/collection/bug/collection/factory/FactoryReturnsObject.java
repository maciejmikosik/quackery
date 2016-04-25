package org.quackery.contract.collection.bug.collection.factory;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class FactoryReturnsObject {
  public static <E> Object create(Collection<? extends E> collection) {
    return new MutableList(collection);
  }
}
