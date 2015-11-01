package org.quackery.contract.collection.bug.collection.constructor;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class DefaultConstructorIsMissing<E> extends MutableList<E> {
  public DefaultConstructorIsMissing(Collection<E> collection) {
    super(collection);
  }
}
