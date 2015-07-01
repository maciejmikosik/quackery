package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.Iterator;

public class test_mutable_collection_contract {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class)
        .mutable();
    for (Class<?> bug : asList(
        IteratorRemovesHasNoEffect.class,
        IteratorRemovesSwallowsException.class,
        IteratorRemovesThrowsException.class,
        IteratorRemovesThrowsInverted.class,
        IteratorRemovesIgnoresSecondCall.class,
        AddHasNoEffect.class,
        AddAllHasNoEffect.class,
        ClearHasNoEffect.class)) {
      assertFailure(contract.test(bug));
      assertFailure(contract.withFactory("create").test(asCollectionFactory(bug)));
    }
  }

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

  public static class AddHasNoEffect<E> extends MutableList<E> {
    public AddHasNoEffect() {}

    public AddHasNoEffect(Collection<E> collection) {
      super(collection);
    }

    public boolean add(E e) {
      return true;
    }
  }

  public static class AddAllHasNoEffect<E> extends MutableList<E> {
    public AddAllHasNoEffect() {}

    public AddAllHasNoEffect(Collection<E> collection) {
      super(collection);
    }

    public boolean addAll(Collection<? extends E> collection) {
      return true;
    }
  }

  public static class ClearHasNoEffect<E> extends MutableList<E> {
    public ClearHasNoEffect() {}

    public ClearHasNoEffect(Collection<E> collection) {
      super(collection);
    }

    public void clear() {}
  }
}
