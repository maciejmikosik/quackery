package org.quackery.contract.collection.bug.collection.constructor;

import java.util.Collection;

import org.quackery.contract.collection.correct.MutableList;

public class DefaultConstructorIsHidden<E> extends MutableList<E> {
  DefaultConstructorIsHidden() {}

  public DefaultConstructorIsHidden(Collection<E> collection) {
    super(collection);
  }
}
