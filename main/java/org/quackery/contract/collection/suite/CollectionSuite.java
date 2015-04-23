package org.quackery.contract.collection.suite;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.assertThat;
import static org.quackery.AssertionException.fail;
import static org.quackery.AssumptionException.assume;
import static org.quackery.contract.collection.Collections.copy;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;
import static org.quackery.contract.collection.Element.b;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.quackery.AssertionException;
import org.quackery.AssumptionException;
import org.quackery.Case;
import org.quackery.Test;
import org.quackery.contract.collection.Creator;

public class CollectionSuite {
  public static Test implementsCollectionInterface(final Class<?> type) {
    return new Case("implements Collection interface") {
      public void run() throws Throwable {
        assertThat(Collection.class.isAssignableFrom(type));
      }
    };
  }

  public static Test defaultConstructorIsDeclared(final Class<?> type) {
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

  public static Test defaultConstructorIsPublic(final Class<?> type) {
    return new Case("is public") {
      public void run() {
        try {
          Constructor<?> constructor = type.getDeclaredConstructor();
          assertThat(Modifier.isPublic(constructor.getModifiers()));
        } catch (NoSuchMethodException e) {
          assume(false);
        }
      }
    };
  }

  public static Test defaultConstructorCreatesEmptyCollection(final Class<?> type) {
    return new Case("creates empty collection") {
      public void run() throws Throwable {
        try {
          assume(Collection.class.isAssignableFrom(type));
          Collection<?> collection = (Collection<?>) type.getConstructor().newInstance();
          assertEquals(collection.toArray(), new Object[0]);
        } catch (NoSuchMethodException e) {
          assume(false);
        }
      }
    };
  }

  public static Test copyConstructorIsDeclared(final Class<?> type) {
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

  public static Test copyConstructorIsPublic(final Class<?> type) {
    return new Case("is public") {
      public void run() {
        try {
          Constructor<?> constructor = type.getDeclaredConstructor(Collection.class);
          assertThat(Modifier.isPublic(constructor.getModifiers()));
        } catch (NoSuchMethodException e) {
          assume(false);
        }
      }
    };
  }

  public static Test factoryIsDeclared(final Class<?> type, final String methodName) {
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

  public static Test factoryIsPublic(final Class<?> type, final String methodName) {
    return new Case("is public") {
      public void run() {
        try {
          Method method = type.getDeclaredMethod(methodName, Collection.class);
          assertThat(Modifier.isPublic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
          assume(false);
        }
      }
    };
  }

  public static Test factoryIsStatic(final Class<?> type, final String methodName) {
    return new Case("is static") {
      public void run() {
        try {
          Method method = type.getDeclaredMethod(methodName, Collection.class);
          assertThat(Modifier.isStatic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
          assume(false);
        }
      }
    };
  }

  public static Test factoryReturnsCollection(final Class<?> type, final String methodName) {
    return new Case("returns collection") {
      public void run() {
        try {
          Method method = type.getDeclaredMethod(methodName, Collection.class);
          assertThat(Collection.class.isAssignableFrom(method.getReturnType()));
        } catch (NoSuchMethodException e) {
          assume(false);
        }
      }
    };
  }

  public static Test creatorCanCreateCollectionWithOneElement(final Creator creator) {
    return new Case("can create collection with 1 element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        Collection<?> collection = creator.create(Collection.class, copy(original));
        assertEquals(collection.toArray(), original.toArray());
      }
    };
  }

  public static Test creatorFailsForNullArgument(final Creator creator) {
    return new Case("fails for null argument") {
      public void run() throws Throwable {
        try {
          creator.create(Object.class, null);
          fail();
        } catch (InvocationTargetException e) {
          try {
            throw e.getCause();
          } catch (NullPointerException f) {}
        }
      }
    };
  }

  public static Test creatorMakesDefensiveCopy(final Creator creator) {
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

  public static Test creatorDoesNotModifyArgument(final Creator creator) {
    return new Case("does not modify argument") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        ArrayList<Object> argument = copy(original);
        creator.create(Object.class, argument);
        assertEquals(argument.toArray(), original.toArray());
      }
    };
  }

  public static Test sizeReturnsOneIfCollectionHasOneElement(final Creator creator) {
    return new Case("returns 1 if collection has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertThat(collection.size() == 1);
      }
    };
  }

  public static Test sizeReturnsZeroIfCollectionIsEmpty(final Creator creator) {
    return new Case("returns 0 if collection is empty") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList());
        assertThat(collection.size() == 0);
      }
    };
  }

  public static Test isEmptyReturnsFalseIfCollectionHasOneElement(final Creator creator) {
    return new Case("returns false if collection has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertThat(!collection.isEmpty());
      }
    };
  }

  public static Test isEmptyReturnsTrueIfCollectionIsEmpty(final Creator creator) {
    return new Case("returns true if collection is empty") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList());
        assertThat(collection.isEmpty());
      }
    };
  }

  public static Test containsReturnsFalseIfCollectionDoesNotContainElement(final Creator creator) {
    return new Case("returns false if collection does not contain element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertThat(!collection.contains(b));
      }
    };
  }

  public static Test containsReturnsTrueIfCollectionContainsElement(final Creator creator) {
    return new Case("returns true if collection contains element") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        assertThat(collection.contains(a));
      }
    };
  }

  public static Test iteratorTraversesEmptyCollection(final Creator creator) {
    return new Case("traverses empty collection") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList());
        Iterator<?> iterator = collection.iterator();
        assertThat(iterator != null);
        assertThat(!iterator.hasNext());
        try {
          iterator.next();
          fail();
        } catch (NoSuchElementException e) {}
      }
    };
  }

  public static Test iteratorTraversesSingletonCollection(final Creator creator) {
    return new Case("traverses singleton collection") {
      public void run() throws Throwable {
        Collection<?> collection = creator.create(Collection.class, newArrayList(a));
        Iterator<?> iterator = collection.iterator();
        assertThat(iterator != null);
        assertThat(iterator.hasNext());
        assertEquals(iterator.next(), a);
        assertThat(!iterator.hasNext());
        try {
          iterator.next();
          fail();
        } catch (NoSuchElementException e) {}
      }
    };
  }

  public static Test iteratorRemovesNoElementsFromEmptyCollection(final Creator creator) {
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

  public static Test iteratorRemovesElementFromSingletonCollection(final Creator creator) {
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
        assertThat(array.length == 0);
      }
    };
  }

  public static Test iteratorRemovesForbidsConsecutiveCalls(final Creator creator) {
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
}
