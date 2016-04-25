package org.quackery.contract.collection.bug.collection;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class ContainsNegates<E> extends MutableList<E> {
  public ContainsNegates() {}

  public ContainsNegates(Collection<E> collection) {
    super(collection);
  }

  public boolean contains(Object o) {
    return !super.contains(o);
  }
}
