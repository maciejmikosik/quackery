package org.quackery.contract.collection.bug.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.correct.MutableList;

public class CreatorAddsElement<E> extends MutableList<E> {
  public CreatorAddsElement() {}

  public CreatorAddsElement(Collection<E> collection) {
    super(add((E) "x", collection));
  }

  private static <E> Collection<E> add(E element, Collection<E> collection) {
    List<E> added = new ArrayList<>(collection);
    added.add(element);
    return added;
  }
}
