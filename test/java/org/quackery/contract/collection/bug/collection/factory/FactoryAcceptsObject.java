package org.quackery.contract.collection.bug.collection.factory;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.correct.MutableList;

public class FactoryAcceptsObject {
  public static <E> List<E> create(Object collection) {
    return new MutableList((Collection) collection);
  }
}
