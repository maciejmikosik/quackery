package org.quackery.contract.collection.bug.collection.constructor;

import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class CopyConstructorIsHidden<E> extends MutableList<E> {
  public CopyConstructorIsHidden() {}

  CopyConstructorIsHidden(Collection<E> collection) {
    super(collection);
  }
}
