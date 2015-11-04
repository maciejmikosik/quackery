package org.quackery.contract.collection.bug.collection.factory;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.MutableList;

public class FactoryIsNotStatic {
  public <E> List<E> create(Collection<? extends E> collection) {
    return new MutableList(collection);
  }
}
