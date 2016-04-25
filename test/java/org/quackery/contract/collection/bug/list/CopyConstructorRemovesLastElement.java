package org.quackery.contract.collection.bug.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.correct.MutableList;

public class CopyConstructorRemovesLastElement<E> extends MutableList<E> {
  public CopyConstructorRemovesLastElement() {}

  public CopyConstructorRemovesLastElement(Collection<E> collection) {
    super(withoutLast(collection));
  }

  private static <E> Collection<E> withoutLast(Collection<E> collection) {
    List<E> list = new ArrayList<>(collection);
    if (!list.isEmpty()) {
      list.remove(list.size() - 1);
    }
    return list;
  }
}
