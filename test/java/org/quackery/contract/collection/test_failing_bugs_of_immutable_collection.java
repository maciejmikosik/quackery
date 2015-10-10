package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.Iterator;

public class test_failing_bugs_of_immutable_collection {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .immutable();
    for (Class<?> bug : asList(
        IteratorRemovesElement.class,
        IteratorDoesNotThrowException.class,
        AddAddsElement.class,
        AddDoesNotThrowException.class,
        RemoveRemovesElement.class,
        RemoveDoesNotThrowException.class,
        AddAllAddsElements.class,
        AddAllDoesNotThrowException.class,
        RemoveAllRemovesElements.class,
        RemoveAllDoesNotThrowException.class,
        RetainAllRetainsElements.class,
        RetainAllDoesNotThrowException.class,
        ClearClearsElements.class,
        ClearDoesNotThrowException.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }

  public static class IteratorRemovesElement<E> extends ImmutableList<E> {
    public IteratorRemovesElement() {}

    public IteratorRemovesElement(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = delegate.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          iterator.remove();
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  public static class IteratorDoesNotThrowException<E> extends ImmutableList<E> {
    public IteratorDoesNotThrowException() {}

    public IteratorDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = delegate.iterator();
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

  public static class AddAddsElement<E> extends ImmutableList<E> {
    public AddAddsElement() {}

    public AddAddsElement(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      delegate.add(e);
      throw new UnsupportedOperationException();
    }
  }

  public static class AddDoesNotThrowException<E> extends ImmutableList<E> {
    public AddDoesNotThrowException() {}

    public AddDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      return false;
    }
  }

  public static class RemoveRemovesElement<E> extends ImmutableList<E> {
    public RemoveRemovesElement() {}

    public RemoveRemovesElement(Collection<E> collection) {
      super(collection);
    }

    public boolean remove(Object o) {
      delegate.remove(o);
      throw new UnsupportedOperationException();
    }
  }

  public static class RemoveDoesNotThrowException<E> extends ImmutableList<E> {
    public RemoveDoesNotThrowException() {}

    public RemoveDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public boolean remove(Object o) {
      return false;
    }
  }

  public static class AddAllAddsElements<E> extends ImmutableList<E> {
    public AddAllAddsElements() {}

    public AddAllAddsElements(Collection<E> collection) {
      super(collection);
    }

    public boolean addAll(Collection<? extends E> c) {
      delegate.addAll(c);
      throw new UnsupportedOperationException();
    }
  }

  public static class AddAllDoesNotThrowException<E> extends ImmutableList<E> {
    public AddAllDoesNotThrowException() {}

    public AddAllDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public boolean addAll(Collection<? extends E> c) {
      return false;
    }
  }

  public static class RemoveAllRemovesElements<E> extends ImmutableList<E> {
    public RemoveAllRemovesElements() {}

    public RemoveAllRemovesElements(Collection<E> collection) {
      super(collection);
    }

    public boolean removeAll(Collection<?> c) {
      delegate.removeAll(c);
      throw new UnsupportedOperationException();
    }
  }

  public static class RemoveAllDoesNotThrowException<E> extends ImmutableList<E> {
    public RemoveAllDoesNotThrowException() {}

    public RemoveAllDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public boolean removeAll(Collection<?> c) {
      return false;
    }
  }

  public static class RetainAllRetainsElements<E> extends ImmutableList<E> {
    public RetainAllRetainsElements() {}

    public RetainAllRetainsElements(Collection<E> collection) {
      super(collection);
    }

    public boolean retainAll(Collection<?> c) {
      delegate.retainAll(c);
      throw new UnsupportedOperationException();
    }
  }

  public static class RetainAllDoesNotThrowException<E> extends ImmutableList<E> {
    public RetainAllDoesNotThrowException() {}

    public RetainAllDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public boolean retainAll(Collection<?> c) {
      return false;
    }
  }

  public static class ClearClearsElements<E> extends ImmutableList<E> {
    public ClearClearsElements() {}

    public ClearClearsElements(Collection<E> collection) {
      super(collection);
    }

    public void clear() {
      delegate.clear();
      throw new UnsupportedOperationException();
    }
  }

  public static class ClearDoesNotThrowException<E> extends ImmutableList<E> {
    public ClearDoesNotThrowException() {}

    public ClearDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public void clear() {}
  }
}
