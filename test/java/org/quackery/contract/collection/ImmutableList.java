package org.quackery.contract.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ImmutableList<E> implements List<E> {
  protected List<E> delegate;

  public ImmutableList() {
    delegate = new ArrayList<>();
  }

  public ImmutableList(Collection<E> collection) {
    delegate = new ArrayList<>(collection);
  }

  public int size() {
    return delegate.size();
  }

  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  public boolean contains(Object o) {
    return delegate.contains(o);
  }

  public Iterator<E> iterator() {
    return new UnmodifiableIterator(delegate.listIterator());
  }

  public Object[] toArray() {
    return delegate.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return delegate.toArray(a);
  }

  public boolean add(E e) {
    throw new UnsupportedOperationException();
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  public boolean containsAll(Collection<?> c) {
    return delegate.containsAll(c);
  }

  public boolean addAll(Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public E get(int index) {
    return delegate.get(index);
  }

  public E set(int index, E element) {
    throw new UnsupportedOperationException();
  }

  public void add(int index, E element) {
    throw new UnsupportedOperationException();
  }

  public E remove(int index) {
    throw new UnsupportedOperationException();
  }

  public int indexOf(Object o) {
    return delegate.indexOf(o);
  }

  public int lastIndexOf(Object o) {
    return delegate.lastIndexOf(o);
  }

  public ListIterator<E> listIterator() {
    return new UnmodifiableIterator(delegate.listIterator());
  }

  public ListIterator<E> listIterator(int index) {
    return new UnmodifiableIterator(delegate.listIterator(index));
  }

  public List<E> subList(int fromIndex, int toIndex) {
    ImmutableList<E> subList = new ImmutableList<>();
    subList.delegate = delegate.subList(fromIndex, toIndex);
    return subList;
  }

  public boolean equals(Object o) {
    return delegate.equals(o);
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  protected static class UnmodifiableIterator<E> implements ListIterator<E> {
    protected ListIterator<E> delegateIterator;

    protected UnmodifiableIterator(ListIterator<E> delegateIterator) {
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
      throw new UnsupportedOperationException();
    }

    public void set(E e) {
      throw new UnsupportedOperationException();
    }

    public void add(E e) {
      throw new UnsupportedOperationException();
    }
  }
}
