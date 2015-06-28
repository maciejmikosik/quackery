package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asCollectionFactory;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class test_collection_contract {
  public void detects_bugs() {
    CollectionContract contract = quacksLike(Collection.class);

    for (Class<?> specificBug : asList(
        DefaultConstructorIsMissing.class,
        DefaultConstructorIsHidden.class,
        DefaultConstructorAddsElement.class,
        CopyConstructorIsMissing.class,
        CopyConstructorIsHidden.class,
        FactoryIsMissing.class,
        FactoryIsHidden.class,
        FactoryIsNotStatic.class,
        FactoryReturnsObject.class,
        FactoryHasDifferentName.class,
        FactoryAcceptsObject.class)) {
      assertFailure(contract.test(specificBug));
    }

    for (Class<?> commonBug : asList(
        CreatorCreatesEmpty.class,
        CreatorAddsElement.class,
        CreatorAcceptsNull.class,
        CreatorMakesNoDefensiveCopy.class,
        CreatorModifiesArgument.class,
        CreatorCreatesFixed.class,
        ToArrayReturnsEmpty.class,
        ToArrayReturnsUnknownElement.class,
        ToArrayReturnsNull.class,
        SizeReturnsZero.class,
        SizeReturnsOne.class,
        IsEmptyReturnsTrue.class,
        IsEmptyReturnsFalse.class,
        IsEmptyNegates.class,
        ContainsReturnsTrue.class,
        ContainsReturnsFalse.class,
        ContainsNegates.class,
        IteratorReturnsNull.class,
        IteratorIsEmpty.class,
        IteratorHasUnknownElement.class,
        IteratorHasUnknownElementIfCollectionIsEmpty.class,
        IteratorRepeatsFirstElementInfinitely.class,
        IteratorInsertsFirstElement.class,
        IteratorInsertsLastElement.class,
        IteratorSkipsFirstElement.class,
        IteratorHasNextAlways.class,
        IteratorHasNextNever.class,
        IteratorHasNextNegates.class,
        IteratorHasNextIfCollectionIsEmpty.class,
        IteratorHasNextIfCollectionIsNotEmpty.class,
        IteratorNextReturnsUnknownElementAfterTraversing.class,
        IteratorNextReturnsNullAfterTraversing.class)) {
      assertFailure(contract.test(commonBug));
      assertFailure(contract.test(asCollectionFactory(commonBug)));
    }
  }

  public static class DefaultConstructorIsMissing<E> extends MutableList<E> {
    public DefaultConstructorIsMissing(Collection<E> collection) {
      super(collection);
    }
  }

  public static class DefaultConstructorIsHidden<E> extends MutableList<E> {
    DefaultConstructorIsHidden() {}

    public DefaultConstructorIsHidden(Collection<E> collection) {
      super(collection);
    }
  }

  public static class DefaultConstructorAddsElement<E> extends MutableList<E> {
    public DefaultConstructorAddsElement() {
      super(asList((E) newObject("x")));
    }

    public DefaultConstructorAddsElement(Collection<E> collection) {
      super(collection);
    }
  }

  public static class CopyConstructorIsMissing<E> extends MutableList<E> {
    public CopyConstructorIsMissing() {}
  }

  public static class CopyConstructorIsHidden<E> extends MutableList<E> {
    public CopyConstructorIsHidden() {}

    CopyConstructorIsHidden(Collection<E> collection) {
      super(collection);
    }
  }

  public static class FactoryIsMissing {}

  public static class FactoryIsHidden {
    static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  public static class FactoryIsNotStatic {
    public <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  public static class FactoryReturnsObject {
    public static <E> Object create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  public static class FactoryHasDifferentName {
    public static <E> List<E> differentName(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  public static class FactoryAcceptsObject {
    public static <E> List<E> create(Object collection) {
      return new MutableList((Collection) collection);
    }
  }

  public static class CreatorCreatesEmpty<E> extends MutableList<E> {
    public CreatorCreatesEmpty() {}

    public CreatorCreatesEmpty(Collection<E> collection) {
      if (collection == null) {
        throw new NullPointerException();
      }
    }
  }

  public static class CreatorAddsElement<E> extends MutableList<E> {
    public CreatorAddsElement() {}

    public CreatorAddsElement(Collection<E> collection) {
      super(add((E) newObject("x"), collection));
    }

    private static <E> Collection<E> add(E element, Collection<E> collection) {
      List<E> added = new ArrayList<>(collection);
      added.add(element);
      return added;
    }
  }

  public static class CreatorAcceptsNull<E> extends MutableList<E> {
    public CreatorAcceptsNull() {}

    public CreatorAcceptsNull(Collection<E> collection) {
      super(unnull(collection));
    }

    private static <E> Collection<E> unnull(Collection<E> collection) {
      return collection == null
          ? Arrays.<E> asList()
          : collection;
    }
  }

  public static class CreatorMakesNoDefensiveCopy<E> extends MutableList<E> {
    public CreatorMakesNoDefensiveCopy() {}

    public CreatorMakesNoDefensiveCopy(Collection<E> collection) {
      super(collection);
      delegate = (List<E>) collection;
    }
  }

  public static class CreatorModifiesArgument<E> extends MutableList<E> {
    public CreatorModifiesArgument() {}

    public CreatorModifiesArgument(Collection<E> collection) {
      super(collection);
      collection.add((E) newObject("x"));
    }
  }

  public static class CreatorCreatesFixed<E> extends MutableList<E> {
    public CreatorCreatesFixed() {}

    public CreatorCreatesFixed(Collection<E> collection) {
      super(asList((E) newObject("x")));
      if (collection == null) {
        throw new NullPointerException();
      }
    }
  }

  public static class ToArrayReturnsEmpty<E> extends MutableList<E> {
    public ToArrayReturnsEmpty() {}

    public ToArrayReturnsEmpty(Collection<E> collection) {
      super(collection);
    }

    public Object[] toArray() {
      return new Object[0];
    }
  }

  public static class ToArrayReturnsUnknownElement<E> extends MutableList<E> {
    public ToArrayReturnsUnknownElement() {}

    public ToArrayReturnsUnknownElement(Collection<E> collection) {
      super(collection);
    }

    public Object[] toArray() {
      return new Object[] { newObject("x") };
    }
  }

  public static class ToArrayReturnsNull<E> extends MutableList<E> {
    public ToArrayReturnsNull() {}

    public ToArrayReturnsNull(Collection<E> collection) {
      super(collection);
    }

    public Object[] toArray() {
      return null;
    }
  }

  public static class SizeReturnsZero<E> extends MutableList<E> {
    public SizeReturnsZero() {}

    public SizeReturnsZero(Collection<E> collection) {
      super(collection);
    }

    public int size() {
      return 0;
    }
  }

  public static class SizeReturnsOne<E> extends MutableList<E> {
    public SizeReturnsOne() {}

    public SizeReturnsOne(Collection<E> collection) {
      super(collection);
    }

    public int size() {
      return 1;
    }
  }

  public static class IsEmptyReturnsTrue<E> extends MutableList<E> {
    public IsEmptyReturnsTrue() {}

    public IsEmptyReturnsTrue(Collection<E> collection) {
      super(collection);
    }

    public boolean isEmpty() {
      return true;
    }
  }

  public static class IsEmptyReturnsFalse<E> extends MutableList<E> {
    public IsEmptyReturnsFalse() {}

    public IsEmptyReturnsFalse(Collection<E> collection) {
      super(collection);
    }

    public boolean isEmpty() {
      return false;
    }
  }

  public static class IsEmptyNegates<E> extends MutableList<E> {
    public IsEmptyNegates() {}

    public IsEmptyNegates(Collection<E> collection) {
      super(collection);
    }

    public boolean isEmpty() {
      return !super.isEmpty();
    }
  }

  public static class ContainsReturnsTrue<E> extends MutableList<E> {
    public ContainsReturnsTrue() {}

    public ContainsReturnsTrue(Collection<E> collection) {
      super(collection);
    }

    public boolean contains(Object o) {
      return true;
    }
  }

  public static class ContainsReturnsFalse<E> extends MutableList<E> {
    public ContainsReturnsFalse() {}

    public ContainsReturnsFalse(Collection<E> collection) {
      super(collection);
    }

    public boolean contains(Object o) {
      return false;
    }
  }

  public static class ContainsNegates<E> extends MutableList<E> {
    public ContainsNegates() {}

    public ContainsNegates(Collection<E> collection) {
      super(collection);
    }

    public boolean contains(Object o) {
      return !super.contains(o);
    }
  }

  public static class IteratorReturnsNull<E> extends MutableList<E> {
    public IteratorReturnsNull() {}

    public IteratorReturnsNull(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      return null;
    }
  }

  public static class IteratorIsEmpty<E> extends MutableList<E> {
    public IteratorIsEmpty() {}

    public IteratorIsEmpty(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      return new ArrayList<E>().iterator();
    }
  }

  public static class IteratorHasUnknownElement<E> extends MutableList<E> {
    public IteratorHasUnknownElement() {}

    public IteratorHasUnknownElement(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      return new ArrayList<E>(asList((E) newObject("x"))).iterator();
    }
  }

  public static class IteratorHasUnknownElementIfCollectionIsEmpty<E> extends MutableList<E> {
    public IteratorHasUnknownElementIfCollectionIsEmpty() {}

    public IteratorHasUnknownElementIfCollectionIsEmpty(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      return isEmpty()
          ? new ArrayList<E>(asList((E) newObject("x"))).iterator()
          : super.iterator();
    }
  }

  public static class IteratorRepeatsFirstElementInfinitely<E> extends MutableList<E> {
    public IteratorRepeatsFirstElementInfinitely() {}

    public IteratorRepeatsFirstElementInfinitely(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      return new Iterator<E>() {
        public boolean hasNext() {
          return delegate.iterator().hasNext();
        }

        public E next() {
          return delegate.iterator().next();
        }

        public void remove() {
          delegate.iterator().remove();
        }
      };
    }
  }

  public static class IteratorInsertsFirstElement<E> extends MutableList<E> {
    public IteratorInsertsFirstElement() {}

    public IteratorInsertsFirstElement(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        private boolean usedExtra = false;

        public boolean hasNext() {
          return !usedExtra || iterator.hasNext();
        }

        public E next() {
          if (!usedExtra) {
            usedExtra = true;
            return (E) newObject("x");
          } else {
            return iterator.next();
          }
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorInsertsLastElement<E> extends MutableList<E> {
    public IteratorInsertsLastElement() {}

    public IteratorInsertsLastElement(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        private boolean usedExtra = false;

        public boolean hasNext() {
          return iterator.hasNext() || !usedExtra;
        }

        public E next() {
          if (iterator.hasNext()) {
            return iterator.next();
          } else if (!usedExtra) {
            usedExtra = true;
            return (E) newObject("x");
          } else {
            return iterator.next();
          }
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorSkipsFirstElement<E> extends MutableList<E> {
    public IteratorSkipsFirstElement() {}

    public IteratorSkipsFirstElement(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      if (iterator.hasNext()) {
        iterator.next();
      }
      return iterator;
    }
  }

  public static class IteratorHasNextAlways<E> extends MutableList<E> {
    public IteratorHasNextAlways() {}

    public IteratorHasNextAlways(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return true;
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorHasNextNever<E> extends MutableList<E> {
    public IteratorHasNextNever() {}

    public IteratorHasNextNever(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return false;
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorHasNextNegates<E> extends MutableList<E> {
    public IteratorHasNextNegates() {}

    public IteratorHasNextNegates(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return !iterator.hasNext();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorHasNextIfCollectionIsEmpty<E> extends MutableList<E> {
    public IteratorHasNextIfCollectionIsEmpty() {}

    public IteratorHasNextIfCollectionIsEmpty(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return isEmpty();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorHasNextIfCollectionIsNotEmpty<E> extends MutableList<E> {
    public IteratorHasNextIfCollectionIsNotEmpty() {}

    public IteratorHasNextIfCollectionIsNotEmpty(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return !isEmpty();
        }

        public E next() {
          return iterator.next();
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorNextReturnsUnknownElementAfterTraversing<E> extends MutableList<E> {
    public IteratorNextReturnsUnknownElementAfterTraversing() {}

    public IteratorNextReturnsUnknownElementAfterTraversing(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          return iterator.hasNext()
              ? iterator.next()
              : (E) newObject("x");
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  public static class IteratorNextReturnsNullAfterTraversing<E> extends MutableList<E> {
    public IteratorNextReturnsNullAfterTraversing() {}

    public IteratorNextReturnsNullAfterTraversing(Collection<E> collection) {
      super(collection);
    }

    public Iterator<E> iterator() {
      final Iterator<E> iterator = super.iterator();
      return new Iterator<E>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }

        public E next() {
          return iterator.hasNext()
              ? iterator.next()
              : null;
        }

        public void remove() {
          iterator.remove();
        }
      };
    }
  }

  private static Object newObject(final String name) {
    return new Object() {
      public String toString() {
        return name;
      }
    };
  }
}
