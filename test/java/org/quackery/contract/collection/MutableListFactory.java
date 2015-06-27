package org.quackery.contract.collection;

import java.util.Collection;
import java.util.List;

public class MutableListFactory {
  public static <E> List<E> create(Collection<? extends E> collection) {
    return new MutableList(collection);
  }

  private MutableListFactory(Object o) {}
}
