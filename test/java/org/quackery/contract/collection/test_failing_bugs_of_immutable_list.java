package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class test_failing_bugs_of_immutable_list {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .implementing(List.class)
        .immutable();
    for (Class<?> bug : asList(
        AddAllIntAddsElements.class,
        AddAllIntDoesNotThrowException.class,
        SetSetsElement.class,
        SetDoesNotThrowException.class,
        AddIntAddsElement.class,
        AddIntDoesNotThrowException.class,
        RemoveIntRemovesElement.class,
        RemoveIntDoesNotThrowException.class,
        ListIteratorRemovesElement.class,
        ListIteratorRemoveDoesNotThrowException.class,
        ListIteratorSetsElement.class,
        ListIteratorSetDoesNotThrowException.class,
        ListIteratorAddsElement.class,
        ListIteratorAddDoesNotThrowException.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }

  public static class AddAllIntAddsElements<E> extends ImmutableList<E> {
    public AddAllIntAddsElements() {}

    public AddAllIntAddsElements(Collection<E> collection) {
      super(collection);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
      delegate.addAll(index, c);
      throw new UnsupportedOperationException();
    }
  }

  public static class AddAllIntDoesNotThrowException<E> extends ImmutableList<E> {
    public AddAllIntDoesNotThrowException() {}

    public AddAllIntDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
      return false;
    }
  }

  public static class SetSetsElement<E> extends ImmutableList<E> {
    public SetSetsElement() {}

    public SetSetsElement(Collection<E> collection) {
      super(collection);
    }

    public E set(int index, E element) {
      delegate.set(index, element);
      throw new UnsupportedOperationException();
    }
  }

  public static class SetDoesNotThrowException<E> extends ImmutableList<E> {
    public SetDoesNotThrowException() {}

    public SetDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public E set(int index, E element) {
      return null;
    }
  }

  public static class AddIntAddsElement<E> extends ImmutableList<E> {
    public AddIntAddsElement() {}

    public AddIntAddsElement(Collection<E> collection) {
      super(collection);
    }

    public void add(int index, E element) {
      delegate.add(index, element);
      throw new UnsupportedOperationException();
    }
  }

  public static class AddIntDoesNotThrowException<E> extends ImmutableList<E> {
    public AddIntDoesNotThrowException() {}

    public AddIntDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public void add(int index, E element) {}
  }

  public static class RemoveIntRemovesElement<E> extends ImmutableList<E> {
    public RemoveIntRemovesElement() {}

    public RemoveIntRemovesElement(Collection<E> collection) {
      super(collection);
    }

    public E remove(int index) {
      delegate.remove(index);
      throw new UnsupportedOperationException();
    }
  }

  public static class RemoveIntDoesNotThrowException<E> extends ImmutableList<E> {
    public RemoveIntDoesNotThrowException() {}

    public RemoveIntDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public E remove(int index) {
      return null;
    }
  }

  public static class ListIteratorRemovesElement<E> extends ImmutableList<E> {
    public ListIteratorRemovesElement() {}

    public ListIteratorRemovesElement(Collection<E> collection) {
      super(collection);
    }

    public ListIterator<E> listIterator() {
      return new UnmodifiableIterator(delegate.listIterator()) {
        public void remove() {
          delegateIterator.remove();
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  public static class ListIteratorRemoveDoesNotThrowException<E> extends ImmutableList<E> {
    public ListIteratorRemoveDoesNotThrowException() {}

    public ListIteratorRemoveDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public ListIterator<E> listIterator() {
      return new UnmodifiableIterator(delegate.listIterator()) {
        public void remove() {}
      };
    }
  }

  public static class ListIteratorSetsElement<E> extends ImmutableList<E> {
    public ListIteratorSetsElement() {}

    public ListIteratorSetsElement(Collection<E> collection) {
      super(collection);
    }

    public ListIterator<E> listIterator() {
      return new UnmodifiableIterator(delegate.listIterator()) {
        public void set(Object e) {
          delegateIterator.set(e);
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  public static class ListIteratorSetDoesNotThrowException<E> extends ImmutableList<E> {
    public ListIteratorSetDoesNotThrowException() {}

    public ListIteratorSetDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public ListIterator<E> listIterator() {
      return new UnmodifiableIterator(delegate.listIterator()) {
        public void set(Object e) {}
      };
    }
  }

  public static class ListIteratorAddsElement<E> extends ImmutableList<E> {
    public ListIteratorAddsElement() {}

    public ListIteratorAddsElement(Collection<E> collection) {
      super(collection);
    }

    public ListIterator<E> listIterator() {
      return new UnmodifiableIterator(delegate.listIterator()) {
        public void add(Object e) {
          delegateIterator.add(e);
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  public static class ListIteratorAddDoesNotThrowException<E> extends ImmutableList<E> {
    public ListIteratorAddDoesNotThrowException() {}

    public ListIteratorAddDoesNotThrowException(Collection<E> collection) {
      super(collection);
    }

    public ListIterator<E> listIterator() {
      return new UnmodifiableIterator(delegate.listIterator()) {
        public void add(Object e) {}
      };
    }
  }
}
