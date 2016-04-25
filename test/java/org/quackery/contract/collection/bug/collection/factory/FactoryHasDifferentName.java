package org.quackery.contract.collection.bug.collection.factory;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.correct.MutableList;

public class FactoryHasDifferentName {
  public static <E> List<E> differentName(Collection<? extends E> collection) {
    return new MutableList(collection);
  }
}
