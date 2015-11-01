package org.quackery.contract.collection.bug.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.MutableList;

public class CopyConstructorRemovesDuplicates<E> extends MutableList<E> {
  public CopyConstructorRemovesDuplicates() {}

  public CopyConstructorRemovesDuplicates(Collection<E> collection) {
    super(withoutDuplicates(collection));
  }

  private static <E> Collection<E> withoutDuplicates(Collection<E> collection) {
    List<E> list = new ArrayList<>();
    for (E element : collection) {
      if (!list.contains(element)) {
        list.add(element);
      }
    }
    return list;
  }
}
