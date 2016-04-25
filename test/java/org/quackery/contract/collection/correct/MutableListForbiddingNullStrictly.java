package org.quackery.contract.collection.correct;

import java.util.Collection;
import java.util.List;

public class MutableListForbiddingNullStrictly<E> extends MutableListForbiddingNullNicely<E> {
  MutableListForbiddingNullStrictly(List<E> delegate) {
    super(delegate);
  }

  public MutableListForbiddingNullStrictly() {}

  public MutableListForbiddingNullStrictly(Collection<E> collection) {
    super(collection);
  }

  public boolean contains(Object o) {
    return super.contains(check(o));
  }

  public boolean remove(Object o) {
    return super.remove(check(o));
  }

  public boolean containsAll(Collection<?> c) {
    return super.containsAll(checkAll(c));
  }

  public boolean removeAll(Collection<?> c) {
    return super.removeAll(checkAll(c));
  }

  public boolean retainAll(Collection<?> c) {
    return super.retainAll(checkAll(c));
  }

  public List<E> subList(int fromIndex, int toIndex) {
    return new MutableListForbiddingNullStrictly(delegate.subList(fromIndex, toIndex));
  }
}
