package org.quackery.contract.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.quackery.contract.Bug;

public class MutableList<E> implements List<E> {
  protected List<E> delegate;

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

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesHasNoEffect<E> extends MutableList<E> {
    public IteratorRemovesHasNoEffect() {}

    public IteratorRemovesHasNoEffect(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {}
      };
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesSwallowsException<E> extends MutableList<E> {
    public IteratorRemovesSwallowsException() {}

    public IteratorRemovesSwallowsException(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          try {
            iterator.remove();
          } catch (IllegalStateException e) {}
        }
      };
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesThrowsException<E> extends MutableList<E> {
    public IteratorRemovesThrowsException() {}

    public IteratorRemovesThrowsException(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          throw new IllegalStateException();
        }
      };
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesThrowsInverted<E> extends MutableList<E> {
    public IteratorRemovesThrowsInverted() {}

    public IteratorRemovesThrowsInverted(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          try {
            iterator.remove();
          } catch (IllegalStateException e) {
            return;
          }
          throw new IllegalStateException();
        }
      };
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class IteratorRemovesIgnoresSecondCall<E> extends MutableList<E> {
    public IteratorRemovesIgnoresSecondCall() {}

    public IteratorRemovesIgnoresSecondCall(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        boolean removed = false;

        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          removed = false;
          return iterator.next();
        }

        public void remove() {
          if (!removed) {
            iterator.remove();
            removed = true;
          }
        }
      };
    }
  }

  @Bug({ Collection.class, Mutable.class })
  public static class ClearHasNoEffect<E> extends MutableList<E> {
    public ClearHasNoEffect() {}

    public ClearHasNoEffect(Collection<E> collection) {
      super(collection);
    }

    public void clear() {}
  }

  @Bug({ List.class, Mutable.class })
  public static class AddHasNoEffect<E> extends MutableList<E> {
    public AddHasNoEffect() {}

    public AddHasNoEffect(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      return true;
    }
  }

  @Bug({ List.class, Mutable.class })
  public static class AddAddsAtTheBegin<E> extends MutableList<E> {
    public AddAddsAtTheBegin() {}

    public AddAddsAtTheBegin(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      super.add(0, e);
      return true;
    }
  }

  @Bug({ List.class, Mutable.class })
  public static class AddReturnsFalse<E> extends MutableList<E> {
    public AddReturnsFalse() {}

    public AddReturnsFalse(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      super.add(e);
      return false;
    }
  }
}
