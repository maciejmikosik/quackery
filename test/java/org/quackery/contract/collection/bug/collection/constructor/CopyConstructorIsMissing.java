package org.quackery.contract.collection.bug.collection.constructor;

import org.quackery.contract.collection.correct.MutableList;

public class CopyConstructorIsMissing<E> extends MutableList<E> {
  public CopyConstructorIsMissing() {}
}
