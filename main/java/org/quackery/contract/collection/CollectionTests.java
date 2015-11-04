package org.quackery.contract.collection;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.assertTrue;
import static org.quackery.AssertionException.fail;
import static org.quackery.AssumptionException.assume;
import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Collections.copy;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;
import static org.quackery.contract.collection.Element.b;
import static org.quackery.contract.collection.Element.c;
import static org.quackery.contract.collection.Element.d;
import static org.quackery.contract.collection.Includes.includeIf;
import static org.quackery.contract.collection.Includes.included;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.quackery.AssertionException;
import org.quackery.AssumptionException;
import org.quackery.Case;
import org.quackery.Test;

public class CollectionTests {
  public static Test test(Configuration configuration, Class<?> type) {
    boolean isList = configuration.isImplementing(List.class);
    boolean hasConstructor = configuration.hasConstructor();
    boolean hasFactory = configuration.hasFactory();
    boolean mutable = configuration.isMutable();
    boolean immutable = configuration.isImmutable();
    String factory = configuration.getFactoryName();
    Creator creator = hasConstructor
        ? new ConstructorCreator(type)
        : new FactoryCreator(type, factory);
    return included(suite(name(type, configuration))
        .test(includeIf(hasConstructor, implementsCollectionInterface(type)))
        .test(includeIf(hasConstructor, suite("provides default constructor")
            .test(defaultConstructorIsDeclared(type))
            .test(defaultConstructorIsPublic(type))
            .test(defaultConstructorCreatesEmptyCollection(type))))
        .test(suite("provides " + name(creator))
            .test(includeIf(hasConstructor, copyConstructorIsDeclared(type)))
            .test(includeIf(hasConstructor, copyConstructorIsPublic(type)))
            .test(includeIf(hasFactory, factoryIsDeclared(type, factory)))
            .test(includeIf(hasFactory, factoryIsPublic(type, factory)))
            .test(includeIf(hasFactory, factoryIsStatic(type, factory)))
            .test(includeIf(hasFactory, factoryReturnsCollection(type, factory)))
            .test(includeIf(hasFactory && isList, factoryReturnsList(type, factory)))
            .test(creatorCanCreateCollectionWithOneElement(creator))
            .test(creatorFailsForNullArgument(creator))
            .test(creatorMakesDefensiveCopy(creator))
            .test(creatorDoesNotModifyArgument(creator))
            .test(includeIf(isList, creatorStoresAllElementsInOrder(creator)))
            .test(includeIf(isList, creatorAllowsDuplicates(creator))))
        .test(suite("overrides size")
            .test(sizeReturnsZeroIfCollectionIsEmpty(creator))
            .test(sizeReturnsOneIfCollectionHasOneElement(creator)))
        .test(suite("overrides isEmpty")
            .test(isEmptyReturnsFalseIfCollectionHasOneElement(creator))
            .test(isEmptyReturnsTrueIfCollectionIsEmpty(creator)))
        .test(suite("overrides contains")
            .test(containsReturnsFalseIfCollectionDoesNotContainElement(creator))
            .test(containsReturnsTrueIfCollectionContainsElement(creator)))
        .test(suite("overrides iterator")
            .test(iteratorTraversesEmptyCollection(creator))
            .test(iteratorTraversesSingletonCollection(creator))
            .test(includeIf(mutable, iteratorRemovesNoElementsFromEmptyCollection(creator)))
            .test(includeIf(mutable, iteratorRemovesElementFromSingletonCollection(creator)))
            .test(includeIf(mutable, iteratorRemovesForbidsConsecutiveCalls(creator)))
            .test(includeIf(immutable, iteratorRemoveThrowsUnsupportedOperationException(creator)))
            .test(includeIf(immutable, iteratorRemoveHasNoSideEffect(creator))))
        .test(includeIf(mutable, suite("overrides add")
            .test(addAddsToEmptyCollection(creator))
            .test(includeIf(isList, addAddsElementAtTheEnd(creator)))
            .test(includeIf(isList, addReturnsTrue(creator)))
            .test(includeIf(isList, addAddsDuplicatedElement(creator)))))
        .test(includeIf(immutable, suite("overrides add")
            .test(addThrowsUnsupportedOperationException(creator))
            .test(addHasNoSideEffect(creator))))
        .test(includeIf(mutable, suite("overrides remove")
            .test(removeRemovesSingleElement(creator))))
        .test(includeIf(immutable, suite("overrides remove")
            .test(removeThrowsUnsupportedOperationException(creator))
            .test(removeHasNoSideEffect(creator))))
        .test(includeIf(mutable, suite("overrides addAll")
            .test(addAllCanAddOneElement(creator))))
        .test(includeIf(immutable, suite("overrides addAll")
            .test(addAllThrowsUnsupportedOperationException(creator))
            .test(addAllHasNoSideEffect(creator))))
        .test(includeIf(immutable && isList, suite("overrides addAll at index")
            .test(addAllIntThrowsUnsupportedOperationException(creator))
            .test(addAllIntHasNoSideEffect(creator))))
        .test(includeIf(immutable, suite("overrides removeAll")
            .test(removeAllThrowsUnsupportedOperationException(creator))
            .test(removeAllHasNoSideEffect(creator))))
        .test(includeIf(immutable, suite("overrides retainAll")
            .test(retainAllThrowsUnsupportedOperationException(creator))
            .test(retainAllHasNoSideEffect(creator))))
        .test(includeIf(mutable, suite("overrides clear")
            .test(clearRemovesElement(creator))))
        .test(includeIf(immutable, suite("overrides clear")
            .test(clearThrowsUnsupportedOperationException(creator))
            .test(clearHasNoSideEffect(creator))))
        .test(includeIf(isList, suite("overrides get")
            .test(getReturnsEachElement(creator))
            .test(getFailsForIndexAboveBound(creator))
            .test(getFailsForIndexBelowBound(creator))))
        .test(includeIf(mutable && isList, suite("overrides set")
            .test(setReplacesSingleElement(creator))
            .test(setReturnsReplacedElement(creator))
            .test(setReplacesElementAtIndex(creator))))
        .test(includeIf(immutable && isList, suite("overrides set")
            .test(setThrowsUnsupportedOperationException(creator))
            .test(setHasNoSideEffect(creator))))
        .test(includeIf(mutable && isList, suite("overrides add at index")
            .test(addIntAddsAtIndex(creator))))
        .test(includeIf(immutable && isList, suite("overrides add at index")
            .test(addIntThrowsUnsupportedOperationException(creator))
            .test(addIntHasNoSideEffect(creator))))
        .test(includeIf(immutable && isList, suite("overrides remove at index")
            .test(removeIntThrowsUnsupportedOperationException(creator))
            .test(removeIntHasNoSideEffect(creator))))
        .test(includeIf(isList, suite("overrides listIterator")
            .test(includeIf(immutable, listIteratorRemoveThrowsUnsupportedOperationException(creator)))
            .test(includeIf(immutable, listIteratorRemoveHasNoSideEffect(creator)))
            .test(includeIf(immutable, listIteratorSetThrowsUnsupportedOperationException(creator)))
            .test(includeIf(immutable, listIteratorSetHasNoSideEffect(creator)))
            .test(includeIf(immutable, listIteratorAddThrowsUnsupportedOperationException(creator)))
            .test(includeIf(immutable, listIteratorAddHasNoSideEffect(creator))))));
  }

  private static String name(Class<?> type, Configuration configuration) {
    StringBuilder builder = new StringBuilder();
    builder.append(type.getName() + " quacks like");
    if (configuration.isMutable()) {
      builder.append(" mutable");
    }
    if (configuration.isImmutable()) {
      builder.append(" immutable");
    }
    builder.append(" ").append(configuration.getCollectionType().getSimpleName().toLowerCase());
    if (configuration.hasFactory()) {
      builder.append(" with factory " + configuration.getFactoryName());
    }
    return builder.toString();
  }

  private static String name(Creator creator) {
    return creator instanceof ConstructorCreator
        ? "copy constructor"
        : "factory method";
  }

  private static Test implementsCollectionInterface(final Class<?> type) {
    return new Case("implements Collection interface") {
      public void run() throws Throwable {
        assertTrue(Collection.class.isAssignableFrom(type));
      }
    };
  }

  private static Test defaultConstructorIsDeclared(final Class<?> type) {
    return new Case("is declared") {
      public void run() {
        try {
          type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
          fail();
        }
      }
    };
  }

  private static Test defaultConstructorIsPublic(final Class<?> type) {
    return new Case("is public") {
      public void run() {
        try {
          Constructor<?> constructor = type.getDeclaredConstructor();
          assertTrue(Modifier.isPublic(constructor.getModifiers()));
        } catch (NoSuchMethodException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test defaultConstructorCreatesEmptyCollection(final Class<?> type) {
    return new Case("creates empty collection") {
      public void run() throws Throwable {
        try {
          assume(Collection.class.isAssignableFrom(type));
          Collection<?> collection = (Collection<?>) type.getConstructor().newInstance();
          assertEquals(collection.toArray(), new Object[0]);
        } catch (NoSuchMethodException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test copyConstructorIsDeclared(final Class<?> type) {
    return new Case("is declared") {
      public void run() {
        try {
          type.getDeclaredConstructor(Collection.class);
        } catch (NoSuchMethodException e) {
          fail();
        }
      }
    };
  }

  private static Test copyConstructorIsPublic(final Class<?> type) {
    return new Case("is public") {
      public void run() {
        try {
          Constructor<?> constructor = type.getDeclaredConstructor(Collection.class);
          assertTrue(Modifier.isPublic(constructor.getModifiers()));
        } catch (NoSuchMethodException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test factoryIsDeclared(final Class<?> type, final String methodName) {
    return new Case("is declared") {
      public void run() {
        try {
          type.getDeclaredMethod(methodName, Collection.class);
        } catch (NoSuchMethodException e) {
          fail();
        }
      }
    };
  }

  private static Test factoryIsPublic(final Class<?> type, final String methodName) {
    return new Case("is public") {
      public void run() {
        try {
          Method method = type.getDeclaredMethod(methodName, Collection.class);
          assertTrue(Modifier.isPublic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test factoryIsStatic(final Class<?> type, final String methodName) {
    return new Case("is static") {
      public void run() {
        try {
          Method method = type.getDeclaredMethod(methodName, Collection.class);
          assertTrue(Modifier.isStatic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test factoryReturnsCollection(final Class<?> type, final String methodName) {
    return new Case("returns collection") {
      public void run() {
        try {
          Method method = type.getDeclaredMethod(methodName, Collection.class);
          assertTrue(Collection.class.isAssignableFrom(method.getReturnType()));
        } catch (NoSuchMethodException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test factoryReturnsList(final Class<?> type, final String factory) {
    return new Case("returns List") {
      public void run() {
        try {
          Method method = type.getMethod(factory, Collection.class);
          assertTrue(List.class.isAssignableFrom(method.getReturnType()));
        } catch (NoSuchMethodException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test creatorCanCreateCollectionWithOneElement(final Creator creator) {
    return new Case("can create collection with 1 element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        Collection<?> collection = creator.create(Collection.class, copy(original));
        assertEquals(collection.toArray(), original.toArray());
      }
    };
  }

  private static Test creatorFailsForNullArgument(final Creator creator) {
    return new Case("fails for null argument") {
      public void run() throws Throwable {
        try {
          creator.create(Object.class, null);
          fail();
        } catch (NullPointerException e) {}
      }
    };
  }

  private static Test creatorMakesDefensiveCopy(final Creator creator) {
    return new Case("makes defensive copy") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        ArrayList<Object> trojan = copy(original);
        Collection<?> collection = creator.create(Collection.class, trojan);
        Object[] array = copy(collection.toArray());
        trojan.clear();
        assertEquals(array, collection.toArray());
      }
    };
  }

  private static Test creatorDoesNotModifyArgument(final Creator creator) {
    return new Case("does not modify argument") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        ArrayList<Object> argument = copy(original);
        creator.create(Object.class, argument);
        assertEquals(argument.toArray(), original.toArray());
      }
    };
  }

  private static Test creatorStoresAllElementsInOrder(final Creator creator) {
    return new Case("stores all elements in order") {
      public void run() throws Throwable {
        run(newArrayList(a, b, c));
        run(newArrayList(a, c, b));
        run(newArrayList(b, a, c));
        run(newArrayList(b, c, a));
        run(newArrayList(c, a, b));
        run(newArrayList(c, b, a));
      }

      private void run(ArrayList<?> order) throws Throwable {
        List<?> list = creator.create(List.class, copy(order));
        assertEquals(copy(list.toArray()), order.toArray());
      }
    };
  }

  private static Test creatorAllowsDuplicates(final Creator creator) {
    return new Case("allows duplicates") {
      public void run() throws Throwable {
        ArrayList<?> original = newArrayList(a, a);
        List<?> list = creator.create(List.class, copy(original));
        assertEquals(copy(list.toArray()), original.toArray());
      }
    };
  }

  private static Test sizeReturnsZeroIfCollectionIsEmpty(final Creator creator) {
    return new Case("returns 0 if collection is empty") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList());
        assertTrue(collection.size() == 0);
      }
    };
  }

  private static Test sizeReturnsOneIfCollectionHasOneElement(final Creator creator) {
    return new Case("returns 1 if collection has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertTrue(collection.size() == 1);
      }
    };
  }

  private static Test isEmptyReturnsFalseIfCollectionHasOneElement(final Creator creator) {
    return new Case("returns false if collection has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertTrue(!collection.isEmpty());
      }
    };
  }

  private static Test isEmptyReturnsTrueIfCollectionIsEmpty(final Creator creator) {
    return new Case("returns true if collection is empty") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList());
        assertTrue(collection.isEmpty());
      }
    };
  }

  private static Test containsReturnsFalseIfCollectionDoesNotContainElement(final Creator creator) {
    return new Case("returns false if collection does not contain element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertTrue(!collection.contains(b));
      }
    };
  }

  private static Test containsReturnsTrueIfCollectionContainsElement(final Creator creator) {
    return new Case("returns true if collection contains element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertTrue(collection.contains(a));
      }
    };
  }

  private static Test iteratorTraversesEmptyCollection(final Creator creator) {
    return new Case("traverses empty collection") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList());
        Iterator<?> iterator = collection.iterator();
        assertTrue(iterator != null);
        assertTrue(!iterator.hasNext());
        try {
          iterator.next();
          fail();
        } catch (NoSuchElementException e) {}
      }
    };
  }

  private static Test iteratorTraversesSingletonCollection(final Creator creator) {
    return new Case("traverses singleton collection") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        Iterator<?> iterator = collection.iterator();
        assertTrue(iterator != null);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), a);
        assertTrue(!iterator.hasNext());
        try {
          iterator.next();
          fail();
        } catch (NoSuchElementException e) {}
      }
    };
  }

  private static Test iteratorRemovesNoElementsFromEmptyCollection(final Creator creator) {
    return new Case("removes no elements from empty collection") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList());
        Iterator<?> iterator = collection.iterator();
        assume(iterator != null);
        try {
          iterator.remove();
          fail();
        } catch (IllegalStateException e) {}
      }
    };
  }

  private static Test iteratorRemovesElementFromSingletonCollection(final Creator creator) {
    return new Case("removes element from singleton collection") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        Iterator<?> iterator = collection.iterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.remove();
        } catch (IllegalStateException e) {
          throw new AssertionException(e);
        }
        Object[] array = collection.toArray();
        assume(array != null);
        assertTrue(array.length == 0);
      }
    };
  }

  private static Test iteratorRemovesForbidsConsecutiveCalls(final Creator creator) {
    return new Case("removes forbids consecutive calls") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        Iterator<?> iterator = collection.iterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.remove();
        } catch (IllegalStateException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.remove();
          fail();
        } catch (IllegalStateException e) {}
      }
    };
  }

  private static Test iteratorRemoveThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        Iterator<?> iterator = collection.iterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.remove();
          fail();
        } catch (IllegalStateException e) {
          throw new AssumptionException(e);
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test iteratorRemoveHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        Iterator<?> iterator = collection.iterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.remove();
        } catch (IllegalStateException e) {
          throw new AssumptionException(e);
        } catch (UnsupportedOperationException e) {}
        assertEquals(copy(collection.toArray()), new Object[] { a });
      }
    };
  }

  private static Test addAddsToEmptyCollection(final Creator creator) {
    return new Case("adds to empty collection") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList());
        boolean added = collection.add(a);
        assertTrue(added);
        assertEquals(copy(collection.toArray()), new Object[] { a });
      }
    };
  }

  private static Test addAddsElementAtTheEnd(final Creator creator) {
    return new Case("adds element at the end") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<Object> list = creator.create(List.class, copy(original));
        original.add(d);
        list.add(d);
        assertEquals(copy(list.toArray()), original.toArray());
      }
    };
  }

  private static Test addReturnsTrue(final Creator creator) {
    return new Case("returns true") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList(a, b, c));
        assertTrue(list.add(d));
      }
    };
  }

  private static Test addAddsDuplicatedElement(final Creator creator) {
    return new Case("adds duplicated element") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList(a));
        list.add(a);
        assertEquals(copy(list.toArray()), new Object[] { a, a });
      }
    };
  }

  private static Test addThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList());
        try {
          collection.add(a);
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test addHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList());
        try {
          collection.add(a);
        } catch (UnsupportedOperationException e) {}
        assertEquals(copy(collection.toArray()), new Object[] {});
      }
    };
  }

  private static Test removeRemovesSingleElement(final Creator creator) {
    return new Case("removes single element") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
        collection.remove(a);
        assertEquals(copy(collection.toArray()), new Object[] {});
      }
    };
  }

  private static Test removeThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.remove(a);
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test removeHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.remove(a);
        } catch (UnsupportedOperationException e) {}
        assertEquals(copy(collection.toArray()), new Object[] { a });
      }
    };
  }

  private static Test addAllCanAddOneElement(final Creator creator) {
    return new Case("can add one element") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList());
        collection.addAll(newArrayList(a));
        assertEquals(collection.toArray(), new Object[] { a });
      }
    };
  }

  private static Test addAllThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList());
        try {
          collection.addAll(newArrayList(a));
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test addAllHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList());
        try {
          collection.addAll(newArrayList(a));
        } catch (UnsupportedOperationException e) {}
        assertEquals(collection.toArray(), new Object[] {});
      }
    };
  }

  private static Test addAllIntThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList());
        try {
          list.addAll(0, newArrayList(a));
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test addAllIntHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList());
        try {
          list.addAll(0, newArrayList(a));
        } catch (UnsupportedOperationException e) {}
        assertEquals(list.toArray(), new Object[] {});
      }
    };
  }

  private static Test removeAllThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.removeAll(newArrayList(a));
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test removeAllHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.removeAll(newArrayList(a));
        } catch (UnsupportedOperationException e) {}
        assertEquals(collection.toArray(), new Object[] { a });
      }
    };
  }

  private static Test retainAllThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.retainAll(newArrayList());
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test retainAllHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.retainAll(newArrayList());
        } catch (UnsupportedOperationException e) {}
        assertEquals(collection.toArray(), new Object[] { a });
      }
    };
  }

  private static Test clearRemovesElement(final Creator creator) {
    return new Case("empties collection if it has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        collection.clear();
        assertEquals(collection.toArray(), new Object[] {});
      }
    };
  }

  private static Test clearThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.clear();
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test clearHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        try {
          collection.clear();
        } catch (UnsupportedOperationException e) {}
        assertEquals(collection.toArray(), new Object[] { a });
      }
    };
  }

  private static Test getReturnsEachElement(final Creator creator) {
    return new Case("returns each element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<?> list = creator.create(List.class, copy(original));
        for (int i = 0; i < original.size(); i++) {
          try {
            assertEquals(list.get(i), original.get(i));
          } catch (IndexOutOfBoundsException e) {
            fail();
          }
        }
      }
    };
  }

  private static Test getFailsForIndexAboveBound(final Creator creator) {
    return new Case("fails for index above bound") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<?> list = creator.create(List.class, copy(original));
        try {
          list.get(original.size());
          fail();
        } catch (IndexOutOfBoundsException e) {}
      }
    };
  }

  private static Test getFailsForIndexBelowBound(final Creator creator) {
    return new Case("fails for index below bound") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<?> list = creator.create(List.class, copy(original));
        try {
          list.get(-1);
          fail();
        } catch (IndexOutOfBoundsException e) {}
      }
    };
  }

  private static Test setReplacesSingleElement(final Creator creator) {
    return new Case("replaces single element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        List<Object> list = creator.create(List.class, copy(original));
        try {
          list.set(0, b);
        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
        assertEquals(copy(list.toArray()), new Object[] { b });
      }
    };
  }

  private static Test setReturnsReplacedElement(final Creator creator) {
    return new Case("returns replaced element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        List<Object> list = creator.create(List.class, copy(original));
        Object returned;
        try {
          returned = list.set(0, b);
        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
        assertEquals(returned, a);
      }
    };
  }

  private static Test setReplacesElementAtIndex(final Creator creator) {
    return new Case("replaces element at index") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<Object> list = creator.create(List.class, copy(original));
        try {
          list.set(2, d);
        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
        assertEquals(copy(list.toArray()), new Object[] { a, b, d });
      }
    };
  }

  private static Test setThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<Object> list = creator.create(List.class, copy(original));
        try {
          list.set(1, d);
          fail();
        } catch (UnsupportedOperationException e) {

        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test setHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<Object> list = creator.create(List.class, copy(original));
        try {
          list.set(1, d);
        } catch (UnsupportedOperationException e) {

        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
        assertEquals(copy(list.toArray()), original.toArray());
      }
    };
  }

  private static Test addIntAddsAtIndex(final Creator creator) {
    return new Case("adds element at index") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<Object> list = creator.create(List.class, copy(original));
        try {
          list.add(1, d);
        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
        assertEquals(copy(list.toArray()), new Object[] { a, d, b, c });
      }
    };
  }

  private static Test addIntThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList());
        try {
          list.add(0, a);
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test addIntHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList());
        try {
          list.add(0, a);
        } catch (UnsupportedOperationException e) {}
        assertEquals(copy(list.toArray()), new Object[] {});
      }
    };
  }

  private static Test removeIntThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList(a));
        try {
          list.remove(0);
          fail();
        } catch (UnsupportedOperationException e) {

        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
      }
    };
  }

  private static Test removeIntHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList(a));
        try {
          list.remove(0);
        } catch (UnsupportedOperationException e) {

        } catch (IndexOutOfBoundsException e) {
          throw new AssumptionException(e);
        }
        assertEquals(copy(list.toArray()), new Object[] { a });
      }
    };
  }

  private static Test listIteratorRemoveThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        List<?> list = creator.create(List.class, newArrayList(a));
        Iterator<?> iterator = list.listIterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.remove();
          fail();
        } catch (IllegalStateException e) {
          throw new AssumptionException(e);
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test listIteratorRemoveHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        List<?> list = creator.create(List.class, newArrayList(a));
        Iterator<?> iterator = list.listIterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.remove();
        } catch (IllegalStateException e) {
          throw new AssumptionException(e);
        } catch (UnsupportedOperationException e) {}
        assertEquals(copy(list.toArray()), new Object[] { a });
      }
    };
  }

  private static Test listIteratorSetThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList(a));
        ListIterator<Object> iterator = list.listIterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.set(b);
          fail();
        } catch (IllegalStateException e) {
          throw new AssumptionException(e);
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test listIteratorSetHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList(a));
        ListIterator<Object> iterator = list.listIterator();
        assume(iterator != null);
        try {
          iterator.next();
        } catch (NoSuchElementException e) {
          throw new AssumptionException(e);
        }
        try {
          iterator.set(b);
        } catch (IllegalStateException e) {
          throw new AssumptionException(e);
        } catch (UnsupportedOperationException e) {}
        assertEquals(copy(list.toArray()), new Object[] { a });
      }
    };
  }

  private static Test listIteratorAddThrowsUnsupportedOperationException(final Creator creator) {
    return new Case("throws UnsupportedOperationException") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList());
        ListIterator<Object> iterator = list.listIterator();
        assume(iterator != null);
        try {
          iterator.add(a);
          fail();
        } catch (UnsupportedOperationException e) {}
      }
    };
  }

  private static Test listIteratorAddHasNoSideEffect(final Creator creator) {
    return new Case("has no side effect") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList());
        ListIterator<Object> iterator = list.listIterator();
        assume(iterator != null);
        try {
          iterator.add(a);
        } catch (UnsupportedOperationException e) {}
        assertEquals(copy(list.toArray()), new Object[] {});
      }
    };
  }
}
