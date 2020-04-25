package org.quackery.contract.collection;

import static java.lang.String.format;
import static org.quackery.Case.newCase;
import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Collections.copy;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;
import static org.quackery.contract.collection.Element.b;
import static org.quackery.contract.collection.Element.c;
import static org.quackery.contract.collection.Element.d;
import static org.quackery.contract.collection.Includes.includeIf;
import static org.quackery.contract.collection.Includes.filterIncluded;
import static org.quackery.report.AssertException.assertEquals;
import static org.quackery.report.AssertException.assertTrue;
import static org.quackery.report.AssertException.fail;
import static org.quackery.report.AssumeException.assume;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.quackery.Test;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;

public class CollectionTests {
  public static Test test(Configuration configuration, Class<?> type) {
    boolean isList = configuration.isImplementing(List.class);
    boolean hasConstructor = configuration.hasConstructor();
    boolean hasFactory = configuration.hasFactory();
    boolean immutable = configuration.isImmutable();
    boolean forbiddingNull = configuration.isForbiddingNull();
    String factory = configuration.getFactoryName();
    Creator creator = hasConstructor
        ? new ConstructorCreator(type)
        : new FactoryCreator(type, factory);
    return filterIncluded(suite(name(type, configuration))
        .add(includeIf(hasConstructor, implementsCollectionInterface(type)))
        .add(includeIf(hasConstructor, suite("provides default constructor")
            .add(defaultConstructorIsDeclared(type))
            .add(defaultConstructorIsPublic(type))
            .add(defaultConstructorCreatesEmptyCollection(type))))
        .add(suite("provides " + name(creator))
            .add(includeIf(hasConstructor, copyConstructorIsDeclared(type)))
            .add(includeIf(hasConstructor, copyConstructorIsPublic(type)))
            .add(includeIf(hasFactory, factoryIsDeclared(type, factory)))
            .add(includeIf(hasFactory, factoryIsPublic(type, factory)))
            .add(includeIf(hasFactory, factoryIsStatic(type, factory)))
            .add(includeIf(hasFactory, factoryReturnsCollection(type, factory)))
            .add(includeIf(hasFactory && isList, factoryReturnsList(type, factory)))
            .add(creatorCanCreateCollectionWithOneElement(creator))
            .add(creatorFailsForNullArgument(creator))
            .add(creatorMakesDefensiveCopy(creator))
            .add(creatorDoesNotModifyArgument(creator))
            .add(includeIf(forbiddingNull, creatorForbidsNullElements(creator)))
            .add(includeIf(!forbiddingNull, creatorAllowsNullElements(creator)))
            .add(includeIf(isList, creatorStoresAllElementsInOrder(creator)))
            .add(includeIf(isList, creatorAllowsDuplicates(creator))))
        .add(suite("overrides size")
            .add(sizeReturnsZeroIfCollectionIsEmpty(creator))
            .add(sizeReturnsOneIfCollectionHasOneElement(creator)))
        .add(suite("overrides isEmpty")
            .add(isEmptyReturnsFalseIfCollectionHasOneElement(creator))
            .add(isEmptyReturnsTrueIfCollectionIsEmpty(creator)))
        .add(suite("overrides contains")
            .add(containsReturnsFalseIfCollectionDoesNotContainElement(creator))
            .add(containsReturnsTrueIfCollectionContainsElement(creator)))
        .add(suite("overrides iterator")
            .add(iteratorTraversesEmptyCollection(creator))
            .add(iteratorTraversesSingletonCollection(creator))
            .add(includeIf(!immutable, iteratorRemovesNoElementsFromEmptyCollection(creator)))
            .add(includeIf(!immutable, iteratorRemovesElementFromSingletonCollection(creator)))
            .add(includeIf(!immutable, iteratorRemovesForbidsConsecutiveCalls(creator)))
            .add(includeIf(immutable, iteratorRemoveThrowsUnsupportedOperationException(creator)))
            .add(includeIf(immutable, iteratorRemoveHasNoSideEffect(creator))))
        .add(includeIf(!immutable, suite("overrides add")
            .add(addAddsToEmptyCollection(creator))
            .add(includeIf(forbiddingNull, addForbidsNullElements(creator)))
            .add(includeIf(!forbiddingNull, addAllowsNullElements(creator)))
            .add(includeIf(isList, addAddsElementAtTheEnd(creator)))
            .add(includeIf(isList, addReturnsTrue(creator)))
            .add(includeIf(isList, addAddsDuplicatedElement(creator)))))
        .add(includeIf(immutable, suite("overrides add")
            .add(addThrowsUnsupportedOperationException(creator))
            .add(addHasNoSideEffect(creator))))
        .add(includeIf(!immutable, suite("overrides remove")
            .add(removeRemovesSingleElement(creator))))
        .add(includeIf(immutable, suite("overrides remove")
            .add(removeThrowsUnsupportedOperationException(creator))
            .add(removeHasNoSideEffect(creator))))
        .add(includeIf(!immutable, suite("overrides addAll")
            .add(addAllCanAddOneElement(creator))))
        .add(includeIf(immutable, suite("overrides addAll")
            .add(addAllThrowsUnsupportedOperationException(creator))
            .add(addAllHasNoSideEffect(creator))))
        .add(includeIf(immutable && isList, suite("overrides addAll at index")
            .add(addAllIntThrowsUnsupportedOperationException(creator))
            .add(addAllIntHasNoSideEffect(creator))))
        .add(includeIf(immutable, suite("overrides removeAll")
            .add(removeAllThrowsUnsupportedOperationException(creator))
            .add(removeAllHasNoSideEffect(creator))))
        .add(includeIf(immutable, suite("overrides retainAll")
            .add(retainAllThrowsUnsupportedOperationException(creator))
            .add(retainAllHasNoSideEffect(creator))))
        .add(includeIf(!immutable, suite("overrides clear")
            .add(clearRemovesElement(creator))))
        .add(includeIf(immutable, suite("overrides clear")
            .add(clearThrowsUnsupportedOperationException(creator))
            .add(clearHasNoSideEffect(creator))))
        .add(includeIf(isList, suite("overrides get")
            .add(getReturnsEachElement(creator))
            .add(getFailsForIndexAboveBound(creator))
            .add(getFailsForIndexBelowBound(creator))))
        .add(includeIf(!immutable && isList, suite("overrides set")
            .add(setReplacesSingleElement(creator))
            .add(setReturnsReplacedElement(creator))
            .add(setReplacesElementAtIndex(creator))))
        .add(includeIf(immutable && isList, suite("overrides set")
            .add(setThrowsUnsupportedOperationException(creator))
            .add(setHasNoSideEffect(creator))))
        .add(includeIf(!immutable && isList, suite("overrides add at index")
            .add(addIntAddsAtIndex(creator))))
        .add(includeIf(immutable && isList, suite("overrides add at index")
            .add(addIntThrowsUnsupportedOperationException(creator))
            .add(addIntHasNoSideEffect(creator))))
        .add(includeIf(immutable && isList, suite("overrides remove at index")
            .add(removeIntThrowsUnsupportedOperationException(creator))
            .add(removeIntHasNoSideEffect(creator))))
        .add(includeIf(isList, suite("overrides listIterator")
            .add(includeIf(immutable, listIteratorRemoveThrowsUnsupportedOperationException(creator)))
            .add(includeIf(immutable, listIteratorRemoveHasNoSideEffect(creator)))
            .add(includeIf(immutable, listIteratorSetThrowsUnsupportedOperationException(creator)))
            .add(includeIf(immutable, listIteratorSetHasNoSideEffect(creator)))
            .add(includeIf(immutable, listIteratorAddThrowsUnsupportedOperationException(creator)))
            .add(includeIf(immutable, listIteratorAddHasNoSideEffect(creator))))));
  }

  private static String name(Class<?> type, Configuration configuration) {
    StringBuilder builder = new StringBuilder();
    builder.append(type.getName() + " quacks like");
    if (configuration.isImmutable()) {
      builder.append(" immutable");
    }
    if (configuration.isForbiddingNull()) {
      builder.append(" forbidding null");
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

  private static Test implementsCollectionInterface(Class<?> type) {
    return newCase("implements Collection interface", () -> {
      assertTrue(Collection.class.isAssignableFrom(type));
    });
  }

  private static Test defaultConstructorIsDeclared(Class<?> type) {
    return newCase("is declared", () -> {
      try {
        type.getDeclaredConstructor();
      } catch (NoSuchMethodException e) {
        fail();
      }
    });
  }

  private static Test defaultConstructorIsPublic(Class<?> type) {
    return newCase("is public", () -> {
      try {
        Constructor<?> constructor = type.getDeclaredConstructor();
        assertTrue(Modifier.isPublic(constructor.getModifiers()));
      } catch (NoSuchMethodException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test defaultConstructorCreatesEmptyCollection(Class<?> type) {
    return newCase("creates empty collection", () -> {
      try {
        assume(Collection.class.isAssignableFrom(type));
        Collection<?> collection = (Collection<?>) type.getConstructor().newInstance();
        assertEquals(collection.toArray(), new Object[0]);
      } catch (NoSuchMethodException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test copyConstructorIsDeclared(Class<?> type) {
    return newCase("is declared", () -> {
      try {
        type.getDeclaredConstructor(Collection.class);
      } catch (NoSuchMethodException e) {
        fail();
      }
    });
  }

  private static Test copyConstructorIsPublic(Class<?> type) {
    return newCase("is public", () -> {
      try {
        Constructor<?> constructor = type.getDeclaredConstructor(Collection.class);
        assertTrue(Modifier.isPublic(constructor.getModifiers()));
      } catch (NoSuchMethodException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test factoryIsDeclared(Class<?> type, String methodName) {
    return newCase("is declared", () -> {
      try {
        type.getDeclaredMethod(methodName, Collection.class);
      } catch (NoSuchMethodException e) {
        fail();
      }
    });
  }

  private static Test factoryIsPublic(Class<?> type, String methodName) {
    return newCase("is public", () -> {
      try {
        Method method = type.getDeclaredMethod(methodName, Collection.class);
        assertTrue(Modifier.isPublic(method.getModifiers()));
      } catch (NoSuchMethodException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test factoryIsStatic(Class<?> type, String methodName) {
    return newCase("is static", () -> {
      try {
        Method method = type.getDeclaredMethod(methodName, Collection.class);
        assertTrue(Modifier.isStatic(method.getModifiers()));
      } catch (NoSuchMethodException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test factoryReturnsCollection(Class<?> type, String methodName) {
    return newCase("returns collection", () -> {
      try {
        Method method = type.getDeclaredMethod(methodName, Collection.class);
        assertTrue(Collection.class.isAssignableFrom(method.getReturnType()));
      } catch (NoSuchMethodException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test factoryReturnsList(Class<?> type, String factory) {
    return newCase("returns List", () -> {
      try {
        Method method = type.getMethod(factory, Collection.class);
        assertTrue(List.class.isAssignableFrom(method.getReturnType()));
      } catch (NoSuchMethodException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test creatorCanCreateCollectionWithOneElement(Creator creator) {
    return newCase("can create collection with 1 element", () -> {
      ArrayList<Object> original = newArrayList(a);
      Collection<?> collection = creator.create(Collection.class, copy(original));
      assertEquals(collection.toArray(), original.toArray());
    });
  }

  private static Test creatorFailsForNullArgument(Creator creator) {
    return newCase("fails for null argument", () -> {
      try {
        creator.create(Object.class, null);
        fail();
      } catch (NullPointerException e) {}
    });
  }

  private static Test creatorMakesDefensiveCopy(Creator creator) {
    return newCase("makes defensive copy", () -> {
      ArrayList<Object> original = newArrayList(a);
      ArrayList<Object> trojan = copy(original);
      Collection<?> collection = creator.create(Collection.class, trojan);
      Object[] array = copy(collection.toArray());
      trojan.clear();
      assertEquals(array, collection.toArray());
    });
  }

  private static Test creatorDoesNotModifyArgument(Creator creator) {
    return newCase("does not modify argument", () -> {
      ArrayList<Object> original = newArrayList(a);
      ArrayList<Object> argument = copy(original);
      creator.create(Object.class, argument);
      assertEquals(argument.toArray(), original.toArray());
    });
  }

  private static Test creatorStoresAllElementsInOrder(Creator creator) {
    return suite("stores all elements in order")
        .add(creatorStoresAllElementsInOrder(creator, newArrayList(a, b, c)))
        .add(creatorStoresAllElementsInOrder(creator, newArrayList(a, c, b)))
        .add(creatorStoresAllElementsInOrder(creator, newArrayList(b, a, c)))
        .add(creatorStoresAllElementsInOrder(creator, newArrayList(b, c, a)))
        .add(creatorStoresAllElementsInOrder(creator, newArrayList(c, a, b)))
        .add(creatorStoresAllElementsInOrder(creator, newArrayList(c, b, a)));
  }

  private static Test creatorStoresAllElementsInOrder(Creator creator, ArrayList<Element> elements) {
    return newCase(format("order %s", elements), () -> {
      List<?> list = creator.create(List.class, copy(elements));
      assertEquals(copy(list.toArray()), elements.toArray());
    });
  }

  private static Test creatorAllowsDuplicates(Creator creator) {
    return newCase("allows duplicates", () -> {
      ArrayList<?> original = newArrayList(a, a);
      List<?> list = creator.create(List.class, copy(original));
      assertEquals(copy(list.toArray()), original.toArray());
    });
  }

  private static Test creatorForbidsNullElements(Creator creator) {
    return newCase("forbids null elements", () -> {
      ArrayList<?> original = newArrayList((Object) null);
      try {
        creator.create(Collection.class, copy(original));
        fail();
      } catch (NullPointerException e) {}
    });
  }

  private static Test creatorAllowsNullElements(Creator creator) {
    return newCase("allows null elements", () -> {
      ArrayList<?> original = newArrayList((Object) null);
      try {
        creator.create(Collection.class, copy(original));
      } catch (NullPointerException e) {
        throw new AssertException(e);
      }
    });
  }

  private static Test sizeReturnsZeroIfCollectionIsEmpty(Creator creator) {
    return newCase("returns 0 if collection is empty", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList());
      assertTrue(collection.size() == 0);
    });
  }

  private static Test sizeReturnsOneIfCollectionHasOneElement(Creator creator) {
    return newCase("returns 1 if collection has 1 element", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      assertTrue(collection.size() == 1);
    });
  }

  private static Test isEmptyReturnsFalseIfCollectionHasOneElement(Creator creator) {
    return newCase("returns false if collection has 1 element", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      assertTrue(!collection.isEmpty());
    });
  }

  private static Test isEmptyReturnsTrueIfCollectionIsEmpty(Creator creator) {
    return newCase("returns true if collection is empty", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList());
      assertTrue(collection.isEmpty());
    });
  }

  private static Test containsReturnsFalseIfCollectionDoesNotContainElement(Creator creator) {
    return newCase("returns false if collection does not contain element", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      assertTrue(!collection.contains(b));
    });
  }

  private static Test containsReturnsTrueIfCollectionContainsElement(Creator creator) {
    return newCase("returns true if collection contains element", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      assertTrue(collection.contains(a));
    });
  }

  private static Test iteratorTraversesEmptyCollection(Creator creator) {
    return newCase("traverses empty collection", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList());
      Iterator<?> iterator = collection.iterator();
      assertTrue(iterator != null);
      assertTrue(!iterator.hasNext());
      try {
        iterator.next();
        fail();
      } catch (NoSuchElementException e) {}
    });
  }

  private static Test iteratorTraversesSingletonCollection(Creator creator) {
    return newCase("traverses singleton collection", () -> {
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
    });
  }

  private static Test iteratorRemovesNoElementsFromEmptyCollection(Creator creator) {
    return newCase("removes no elements from empty collection", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList());
      Iterator<?> iterator = collection.iterator();
      assume(iterator != null);
      try {
        iterator.remove();
        fail();
      } catch (IllegalStateException e) {}
    });
  }

  private static Test iteratorRemovesElementFromSingletonCollection(Creator creator) {
    return newCase("removes element from singleton collection", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      Iterator<?> iterator = collection.iterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.remove();
      } catch (IllegalStateException e) {
        throw new AssertException(e);
      }
      Object[] array = collection.toArray();
      assume(array != null);
      assertTrue(array.length == 0);
    });
  }

  private static Test iteratorRemovesForbidsConsecutiveCalls(Creator creator) {
    return newCase("removes forbids consecutive calls", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      Iterator<?> iterator = collection.iterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.remove();
      } catch (IllegalStateException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.remove();
        fail();
      } catch (IllegalStateException e) {}
    });
  }

  private static Test iteratorRemoveThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      Iterator<?> iterator = collection.iterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.remove();
        fail();
      } catch (IllegalStateException e) {
        throw new AssumeException(e);
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test iteratorRemoveHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      Iterator<?> iterator = collection.iterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.remove();
      } catch (IllegalStateException e) {
        throw new AssumeException(e);
      } catch (UnsupportedOperationException e) {}
      assertEquals(copy(collection.toArray()), new Object[] { a });
    });
  }

  private static Test addAddsToEmptyCollection(Creator creator) {
    return newCase("adds to empty collection", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      boolean added = collection.add(a);
      assertTrue(added);
      assertEquals(copy(collection.toArray()), new Object[] { a });
    });
  }

  private static Test addAddsElementAtTheEnd(Creator creator) {
    return newCase("adds element at the end", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<Object> list = creator.create(List.class, copy(original));
      original.add(d);
      list.add(d);
      assertEquals(copy(list.toArray()), original.toArray());
    });
  }

  private static Test addReturnsTrue(Creator creator) {
    return newCase("returns true", () -> {
      List<Object> list = creator.create(List.class, newArrayList(a, b, c));
      assertTrue(list.add(d));
    });
  }

  private static Test addAddsDuplicatedElement(Creator creator) {
    return newCase("adds duplicated element", () -> {
      List<Object> list = creator.create(List.class, newArrayList(a));
      list.add(a);
      assertEquals(copy(list.toArray()), new Object[] { a, a });
    });
  }

  private static Test addForbidsNullElements(Creator creator) {
    return newCase("forbids null elements", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      try {
        collection.add(null);
        fail();
      } catch (NullPointerException e) {}
    });
  }

  private static Test addAllowsNullElements(Creator creator) {
    return newCase("allows null elements", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      try {
        collection.add(null);
      } catch (NullPointerException e) {
        throw new AssertException(e);
      }
    });
  }

  private static Test addThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      try {
        collection.add(a);
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test addHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      try {
        collection.add(a);
      } catch (UnsupportedOperationException e) {}
      assertEquals(copy(collection.toArray()), new Object[] {});
    });
  }

  private static Test removeRemovesSingleElement(Creator creator) {
    return newCase("removes single element", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
      collection.remove(a);
      assertEquals(copy(collection.toArray()), new Object[] {});
    });
  }

  private static Test removeThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.remove(a);
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test removeHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.remove(a);
      } catch (UnsupportedOperationException e) {}
      assertEquals(copy(collection.toArray()), new Object[] { a });
    });
  }

  private static Test addAllCanAddOneElement(Creator creator) {
    return newCase("can add one element", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      collection.addAll(newArrayList(a));
      assertEquals(collection.toArray(), new Object[] { a });
    });
  }

  private static Test addAllThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      try {
        collection.addAll(newArrayList(a));
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test addAllHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList());
      try {
        collection.addAll(newArrayList(a));
      } catch (UnsupportedOperationException e) {}
      assertEquals(collection.toArray(), new Object[] {});
    });
  }

  private static Test addAllIntThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      List<Object> list = creator.create(List.class, newArrayList());
      try {
        list.addAll(0, newArrayList(a));
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test addAllIntHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      List<Object> list = creator.create(List.class, newArrayList());
      try {
        list.addAll(0, newArrayList(a));
      } catch (UnsupportedOperationException e) {}
      assertEquals(list.toArray(), new Object[] {});
    });
  }

  private static Test removeAllThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.removeAll(newArrayList(a));
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test removeAllHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.removeAll(newArrayList(a));
      } catch (UnsupportedOperationException e) {}
      assertEquals(collection.toArray(), new Object[] { a });
    });
  }

  private static Test retainAllThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.retainAll(newArrayList());
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test retainAllHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      Collection<Object> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.retainAll(newArrayList());
      } catch (UnsupportedOperationException e) {}
      assertEquals(collection.toArray(), new Object[] { a });
    });
  }

  private static Test clearRemovesElement(Creator creator) {
    return newCase("empties collection if it has 1 element", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      collection.clear();
      assertEquals(collection.toArray(), new Object[] {});
    });
  }

  private static Test clearThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.clear();
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test clearHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      Collection<?> collection = creator.create(Collection.class, newArrayList(a));
      try {
        collection.clear();
      } catch (UnsupportedOperationException e) {}
      assertEquals(collection.toArray(), new Object[] { a });
    });
  }

  private static Test getReturnsEachElement(Creator creator) {
    return newCase("returns each element", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<?> list = creator.create(List.class, copy(original));
      for (int i = 0; i < original.size(); i++) {
        try {
          assertEquals(list.get(i), original.get(i));
        } catch (IndexOutOfBoundsException e) {
          fail();
        }
      }
    });
  }

  private static Test getFailsForIndexAboveBound(Creator creator) {
    return newCase("fails for index above bound", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<?> list = creator.create(List.class, copy(original));
      try {
        list.get(original.size());
        fail();
      } catch (IndexOutOfBoundsException e) {}
    });
  }

  private static Test getFailsForIndexBelowBound(Creator creator) {
    return newCase("fails for index below bound", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<?> list = creator.create(List.class, copy(original));
      try {
        list.get(-1);
        fail();
      } catch (IndexOutOfBoundsException e) {}
    });
  }

  private static Test setReplacesSingleElement(Creator creator) {
    return newCase("replaces single element", () -> {
      ArrayList<Object> original = newArrayList(a);
      List<Object> list = creator.create(List.class, copy(original));
      try {
        list.set(0, b);
      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
      assertEquals(copy(list.toArray()), new Object[] { b });
    });
  }

  private static Test setReturnsReplacedElement(Creator creator) {
    return newCase("returns replaced element", () -> {
      ArrayList<Object> original = newArrayList(a);
      List<Object> list = creator.create(List.class, copy(original));
      Object returned;
      try {
        returned = list.set(0, b);
      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
      assertEquals(returned, a);
    });
  }

  private static Test setReplacesElementAtIndex(Creator creator) {
    return newCase("replaces element at index", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<Object> list = creator.create(List.class, copy(original));
      try {
        list.set(2, d);
      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
      assertEquals(copy(list.toArray()), new Object[] { a, b, d });
    });
  }

  private static Test setThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<Object> list = creator.create(List.class, copy(original));
      try {
        list.set(1, d);
        fail();
      } catch (UnsupportedOperationException e) {

      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test setHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<Object> list = creator.create(List.class, copy(original));
      try {
        list.set(1, d);
      } catch (UnsupportedOperationException e) {

      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
      assertEquals(copy(list.toArray()), original.toArray());
    });
  }

  private static Test addIntAddsAtIndex(Creator creator) {
    return newCase("adds element at index", () -> {
      ArrayList<Object> original = newArrayList(a, b, c);
      List<Object> list = creator.create(List.class, copy(original));
      try {
        list.add(1, d);
      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
      assertEquals(copy(list.toArray()), new Object[] { a, d, b, c });
    });
  }

  private static Test addIntThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      List<Object> list = creator.create(List.class, newArrayList());
      try {
        list.add(0, a);
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test addIntHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      List<Object> list = creator.create(List.class, newArrayList());
      try {
        list.add(0, a);
      } catch (UnsupportedOperationException e) {}
      assertEquals(copy(list.toArray()), new Object[] {});
    });
  }

  private static Test removeIntThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      List<Object> list = creator.create(List.class, newArrayList(a));
      try {
        list.remove(0);
        fail();
      } catch (UnsupportedOperationException e) {

      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
    });
  }

  private static Test removeIntHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      List<Object> list = creator.create(List.class, newArrayList(a));
      try {
        list.remove(0);
      } catch (UnsupportedOperationException e) {

      } catch (IndexOutOfBoundsException e) {
        throw new AssumeException(e);
      }
      assertEquals(copy(list.toArray()), new Object[] { a });
    });
  }

  private static Test listIteratorRemoveThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      List<?> list = creator.create(List.class, newArrayList(a));
      Iterator<?> iterator = list.listIterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.remove();
        fail();
      } catch (IllegalStateException e) {
        throw new AssumeException(e);
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test listIteratorRemoveHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      List<?> list = creator.create(List.class, newArrayList(a));
      Iterator<?> iterator = list.listIterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.remove();
      } catch (IllegalStateException e) {
        throw new AssumeException(e);
      } catch (UnsupportedOperationException e) {}
      assertEquals(copy(list.toArray()), new Object[] { a });
    });
  }

  private static Test listIteratorSetThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      List<Object> list = creator.create(List.class, newArrayList(a));
      ListIterator<Object> iterator = list.listIterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.set(b);
        fail();
      } catch (IllegalStateException e) {
        throw new AssumeException(e);
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test listIteratorSetHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      List<Object> list = creator.create(List.class, newArrayList(a));
      ListIterator<Object> iterator = list.listIterator();
      assume(iterator != null);
      try {
        iterator.next();
      } catch (NoSuchElementException e) {
        throw new AssumeException(e);
      }
      try {
        iterator.set(b);
      } catch (IllegalStateException e) {
        throw new AssumeException(e);
      } catch (UnsupportedOperationException e) {}
      assertEquals(copy(list.toArray()), new Object[] { a });
    });
  }

  private static Test listIteratorAddThrowsUnsupportedOperationException(Creator creator) {
    return newCase("throws UnsupportedOperationException", () -> {
      List<Object> list = creator.create(List.class, newArrayList());
      ListIterator<Object> iterator = list.listIterator();
      assume(iterator != null);
      try {
        iterator.add(a);
        fail();
      } catch (UnsupportedOperationException e) {}
    });
  }

  private static Test listIteratorAddHasNoSideEffect(Creator creator) {
    return newCase("has no side effect", () -> {
      List<Object> list = creator.create(List.class, newArrayList());
      ListIterator<Object> iterator = list.listIterator();
      assume(iterator != null);
      try {
        iterator.add(a);
      } catch (UnsupportedOperationException e) {}
      assertEquals(copy(list.toArray()), new Object[] {});
    });
  }
}
