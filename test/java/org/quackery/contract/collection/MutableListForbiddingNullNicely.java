package org.quackery.contract.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class MutableListForbiddingNullNicely<E> extends MutableList<E> {
  MutableListForbiddingNullNicely(List<E> delegate) {
    super(delegate);
  }

  public MutableListForbiddingNullNicely() {}

  public MutableListForbiddingNullNicely(Collection<E> collection) {
    super(checkAll(collection));
  }

  public boolean add(E e) {
    return delegate.add(check(e));
  }

  public boolean addAll(Collection<? extends E> c) {
    return delegate.addAll(checkAll(c));
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    return delegate.addAll(index, checkAll(c));
  }

  public E set(int index, E element) {
    return delegate.set(index, check(element));
  }

  public void add(int index, E element) {
    delegate.add(index, check(element));
  }

  public ListIterator<E> listIterator() {
    return new ForbiddingNullIterator(delegate.listIterator());
  }

  public ListIterator<E> listIterator(int index) {
    return new ForbiddingNullIterator(delegate.listIterator(index));
  }

  public List<E> subList(int fromIndex, int toIndex) {
    return new MutableListForbiddingNullNicely(delegate.subList(fromIndex, toIndex));
  }

  protected static class ForbiddingNullIterator<E> implements ListIterator<E> {
    protected ListIterator<E> delegateIterator;

    protected ForbiddingNullIterator(ListIterator<E> delegateIterator) {
      this.delegateIterator = delegateIterator;
    }

    public boolean hasNext() {
      return delegateIterator.hasNext();
    }

    public E next() {
      return delegateIterator.next();
    }

    public boolean hasPrevious() {
      return delegateIterator.hasPrevious();
    }

    public E previous() {
      return delegateIterator.previous();
    }

    public int nextIndex() {
      return delegateIterator.nextIndex();
    }

    public int previousIndex() {
      return delegateIterator.previousIndex();
    }

    public void remove() {
      delegateIterator.remove();
    }

    public void set(E e) {
      delegateIterator.set(check(e));
    }

    public void add(E e) {
      delegateIterator.add(check(e));
    }
  }

  protected static <E> E check(E object) {
    if (object == null) {
      throw new NullPointerException();
    }
    return object;
  }

  protected static <E> List<E> checkAll(Collection<E> collection) {
    List<E> copy = new ArrayList<>(collection);
    for (E e : copy) {
      check(e);
    }
    return copy;
  }
}
