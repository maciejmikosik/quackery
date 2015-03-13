package org.testanza.testing.bug.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MutableList<E> implements List<E> {
  private final List<E> delegate;

  public MutableList() {
    delegate = new ArrayList<>();
  }

  public MutableList(Collection<E> collection) {
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
    return delegate.iterator();
  }

  public Object[] toArray() {
    return delegate.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return delegate.toArray(a);
  }

  public boolean add(E e) {
    return delegate.add(e);
  }

  public boolean remove(Object o) {
    return delegate.remove(o);
  }

  public boolean containsAll(Collection<?> c) {
    return delegate.containsAll(c);
  }

  public boolean addAll(Collection<? extends E> c) {
    return delegate.addAll(c);
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    return delegate.addAll(index, c);
  }

  public boolean removeAll(Collection<?> c) {
    return delegate.removeAll(c);
  }

  public boolean retainAll(Collection<?> c) {
    return delegate.retainAll(c);
  }

  public void clear() {
    delegate.clear();
  }

  public E get(int index) {
    return delegate.get(index);
  }

  public E set(int index, E element) {
    return delegate.set(index, element);
  }

  public void add(int index, E element) {
    delegate.add(index, element);
  }

  public E remove(int index) {
    return delegate.remove(index);
  }

  public int indexOf(Object o) {
    return delegate.indexOf(o);
  }

  public int lastIndexOf(Object o) {
    return delegate.lastIndexOf(o);
  }

  public ListIterator<E> listIterator() {
    return delegate.listIterator();
  }

  public ListIterator<E> listIterator(int index) {
    return delegate.listIterator(index);
  }

  public List<E> subList(int fromIndex, int toIndex) {
    return delegate.subList(fromIndex, toIndex);
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
}
