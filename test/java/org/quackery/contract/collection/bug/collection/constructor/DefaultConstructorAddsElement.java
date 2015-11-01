package org.quackery.contract.collection.bug.collection.constructor;

import static java.util.Arrays.asList;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class DefaultConstructorAddsElement<E> extends MutableList<E> {
  public DefaultConstructorAddsElement() {
    super(asList((E) "x"));
  }

  public DefaultConstructorAddsElement(Collection<E> collection) {
    super(collection);
  }
}
