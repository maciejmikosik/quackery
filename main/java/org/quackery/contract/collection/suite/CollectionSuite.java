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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.quackery.Case;
import org.quackery.Test;

public class CollectionSuite {
  public static Test implementsCollectionInterface(final Class<?> type) {
    return new Case("implements Collection interface") {
      public void run() throws Throwable {
        assertThat(Collection.class.isAssignableFrom(type));
      }
    };
  }

  public static Test hasDefaultConstructor(final Class<?> type) {
    return new Case("has default constructor") {
      public void run() {
        try {
          type.getConstructor();
        } catch (NoSuchMethodException e) {
          fail();
        }
      }
    };
  }

  public static Test defaultConstructorCreatesEmptyCollection(final Class<?> type) {
    return new Case("default constructor creates empty collection") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Collection<?> collection = (Collection<?>) assumeConstructor(type).newInstance();
        assertEquals(collection.toArray(), new Object[0]);
      }
    };
  }

  public static Test hasCopyConstructor(final Class<?> type) {
    return new Case("has copy constructor") {
      public void run() {
        try {
          type.getConstructor(Collection.class);
        } catch (NoSuchMethodException e) {
          fail();
        }
      }
    };
  }

  public static Test copyConstructorCanCreateCollectionWithOneElement(final Class<?> type) {
    return new Case("copy constructor can create collection with 1 element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        Collection<?> collection = assumeCreateCollection(type, copy(original));
        assertEquals(collection.toArray(), original.toArray());
      }
    };
  }

  public static Test copyConstructorFailsForNullArgument(final Class<?> type) {
    return new Case("copy constructor fails for null argument") {
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

  public static Test copyConstructorMakesDefensiveCopy(final Class<?> type) {
    return new Case("copy constructor makes defensive copy") {
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

  public static Test copyConstructorDoesNotModifyArgument(final Class<?> type) {
    return new Case("copy constructor does not modify argument") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a);
        ArrayList<Object> argument = copy(original);
        assumeCreateCollection(type, argument);
        assertEquals(argument.toArray(), original.toArray());
      }
    };
  }

  public static Test sizeOfCollectionWithOneElementIsOne(final Class<?> type) {
    return new Case("size of collection with 1 element is 1") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList(a));
        assertThat(collection.size() == 1);
      }
    };
  }

  public static Test sizeOfEmptyCollectionIsZero(final Class<?> type) {
    return new Case("size of empty collection is 0") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList());
        assertThat(collection.size() == 0);
      }
    };
  }

  public static Test isEmptyReturnsFalseIfCollectionHasOneElement(final Class<?> type) {
    return new Case("isEmpty returns false if collection has 1 element") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList(a));
        assertThat(!collection.isEmpty());
      }
    };
  }

  public static Test isEmptyReturnsTrueIfCollectionIsEmpty(final Class<?> type) {
    return new Case("isEmpty returns true if collection is empty") {
      public void run() throws Throwable {
        Collection<?> collection = assumeCreateCollection(type, newArrayList());
        assertThat(collection.isEmpty());
      }
    };
  }
}
