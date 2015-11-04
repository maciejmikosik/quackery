package org.quackery.contract.collection.bug.collection;

import java.util.Arrays;
import java.util.Collection;

import org.quackery.contract.collection.MutableList;

public class CreatorAcceptsNull<E> extends MutableList<E> {
  public CreatorAcceptsNull() {}

  public CreatorAcceptsNull(Collection<E> collection) {
    super(unnull(collection));
  }

  private static <E> Collection<E> unnull(Collection<E> collection) {
    return collection == null
        ? Arrays.<E> asList()
        : collection;
  }
}
