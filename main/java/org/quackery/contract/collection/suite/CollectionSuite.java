package org.quackery.contract.collection.suite;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.assertThat;
import static org.quackery.AssertionException.fail;
import static org.quackery.AssumptionException.assume;
import static org.quackery.contract.collection.Assumptions.assumeConstructor;
import static org.quackery.contract.collection.Assumptions.assumeCreateCollection;
import static org.quackery.contract.collection.Collections.copy;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import org.quackery.Case;
import org.quackery.Test;

public class CollectionSuite {
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
        assume(Collection.class.isAssignableFrom(type));
        Collection<?> collection = (Collection<?>) assumeConstructor(type).newInstance();
        assertEquals(collection.toArray(), new Object[0]);
      }
    };
  }

  public static Test instantiatorIsDeclared(final Class<?> type) {
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

  public static Test instantiatorIsPublic(final Class<?> type) {
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

  public static Test instantiatorReturnsCollection(final Class<?> type) {
    return new Case("implements Collection interface") {
      public void run() throws Throwable {
        assertThat(Collection.class.isAssignableFrom(type));
      }
    };
  }

  public static Test instantiatorCanCreateCollectionWithOneElement(final Class<?> type) {
    return new Case("can create collection with 1 element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        Collection<?> collection = assumeCreateCollection(type, copy(original));
        assertEquals(collection.toArray(), original.toArray());
      }
    };
  }

  public static Test instantiatorFailsForNullArgument(final Class<?> type) {
    return new Case("fails for null argument") {
      public void run() throws Throwable {
        try {
          assumeCreateCollection(type, null);
          fail();
        } catch (InvocationTargetException e) {
          try {
            throw e.getCause();
          } catch (NullPointerException f) {}
        }
      }
    };
  }

  public static Test instantiatorMakesDefensiveCopy(final Class<?> type) {
    return new Case("makes defensive copy") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        ArrayList<Object> trojan = copy(original);
        Collection<?> collection = assumeCreateCollection(type, trojan);
        Object[] array = copy(collection.toArray());
        trojan.clear();
        assertEquals(array, collection.toArray());
      }
    };
  }

  public static Test instantiatorDoesNotModifyArgument(final Class<?> type) {
    return new Case("does not modify argument") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        ArrayList<Object> argument = copy(original);
        assumeCreateCollection(type, argument);
        assertEquals(argument.toArray(), original.toArray());
      }
    };
  }

  public static Test sizeReturnsOneIfCollectionHasOneElement(final Class<?> type) {
    return new Case("returns 1 if collection has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList(a));
        assertThat(collection.size() == 1);
      }
    };
  }

  public static Test sizeReturnsZeroIfCollectionIsEmpty(final Class<?> type) {
    return new Case("returns 0 if collection is empty") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList());
        assertThat(collection.size() == 0);
      }
    };
  }

  public static Test isEmptyReturnsFalseIfCollectionHasOneElement(final Class<?> type) {
    return new Case("returns false if collection has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList(a));
        assertThat(!collection.isEmpty());
      }
    };
  }

  public static Test isEmptyReturnsTrueIfCollectionIsEmpty(final Class<?> type) {
    return new Case("returns true if collection is empty") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList());
        assertThat(collection.isEmpty());
      }
    };
  }
}
