package org.quackery.contract.collection.bug.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.quackery.contract.collection.MutableList;

public class CopyConstructorReversesOrder<E> extends MutableList<E> {
  public CopyConstructorReversesOrder() {}

  public CopyConstructorReversesOrder(Collection<E> collection) {
    super(reverse(collection));
  }

  private static <E> Collection<E> reverse(Collection<E> collection) {
    List<E> list = new ArrayList<>(collection);
    Collections.reverse(list);
    return list;
  }
}
