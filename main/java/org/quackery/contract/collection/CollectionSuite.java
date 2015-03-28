package org.quackery.contract.collection;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.assertThat;
import static org.quackery.AssertionException.fail;
import static org.quackery.AssumptionException.assume;
import static org.quackery.Suite.newSuite;
import static org.quackery.contract.Commons.assumeConstructor;
import static org.quackery.contract.Commons.copy;
import static org.quackery.contract.Commons.newArrayList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.quackery.Case;
import org.quackery.Test;

public class CollectionSuite {
  public static Test collectionSuite(Class<?> type) {
    return newSuite("quacks like Collection")
        .test(implementsCollectionInterface(type))
        .test(newSuite("provides default constructor")
            .test(hasDefaultConstructor(type))
            .test(defaultConstructorCreatesEmptyCollection(type)))
        .test(newSuite("provides copy constructor")
            .test(hasCopyConstructor(type))
            .test(copyConstructorCanCreateCollectionWithOneElement(type))
            .test(copyConstructorFailsForNullArgument(type))
            .test(copyConstructorMakesDefensiveCopy(type))
            .test(copyConstructorDoesNotModifyArgument(type)))
        .test(newSuite("overrides size")
            .test(sizeOfEmptyCollectionIsZero(type))
            .test(sizeOfCollectionWithOneElementIsOne(type)))
        .test(newSuite("overrides isEmpty")
            .test(isEmptyReturnsFalseIfCollectionHasOneElement(type))
            .test(isEmptyReturnsTrueIfCollectionIsEmpty(type)));
  }

  private static Test implementsCollectionInterface(final Class<?> type) {
    return new Case("implements Collection interface") {
      public void run() throws Throwable {
        assertThat(Collection.class.isAssignableFrom(type));
      }
    };
  }

  private static Test hasDefaultConstructor(final Class<?> type) {
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

  private static Case defaultConstructorCreatesEmptyCollection(final Class<?> type) {
    return new Case("default constructor creates empty collection") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Collection<?> collection = (Collection<?>) assumeConstructor(type).newInstance();
        assertEquals(collection.toArray(), new Object[0]);
      }
    };
  }

  private static Test hasCopyConstructor(final Class<?> type) {
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

  private static Test copyConstructorCanCreateCollectionWithOneElement(final Class<?> type) {
    return new Case("copy constructor can create collection with 1 element") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        ArrayList<Object> original = newArrayList("a");
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(copy(original));
        assertEquals(collection.toArray(), original.toArray());
      }
    };
  }

  private static Test copyConstructorFailsForNullArgument(final Class<?> type) {
    return new Case("copy constructor fails for null argument") {
      public void run() throws Throwable {
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        try {
          constructor.newInstance((Object) null);
          fail();
        } catch (InvocationTargetException e) {
          if (!(e.getCause() instanceof NullPointerException)) {
            fail();
          }
        }
      }
    };
  }

  private static Test copyConstructorMakesDefensiveCopy(final Class<?> type) {
    return new Case("copy constructor makes defensive copy") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        ArrayList<Object> original = newArrayList("a");
        ArrayList<Object> trojan = copy(original);
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(trojan);
        Object[] array = copy(collection.toArray());
        trojan.clear();
        assertEquals(array, collection.toArray());
      }
    };
  }

  private static Test copyConstructorDoesNotModifyArgument(final Class<?> type) {
    return new Case("copy constructor does not modify argument") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList("a");
        ArrayList<Object> argument = copy(original);
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        constructor.newInstance(argument);
        assertEquals(argument.toArray(), original.toArray());
      }
    };
  }

  private static Test sizeOfCollectionWithOneElementIsOne(final Class<?> type) {
    return new Case("size of collection with 1 element is 1") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(newArrayList("a"));
        assertThat(collection.size() == 1);
      }
    };
  }

  private static Test sizeOfEmptyCollectionIsZero(final Class<?> type) {
    return new Case("size of empty collection is 0") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(newArrayList());
        assertThat(collection.size() == 0);
      }
    };
  }

  private static Test isEmptyReturnsFalseIfCollectionHasOneElement(final Class<?> type) {
    return new Case("isEmpty returns false if collection has 1 element") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(newArrayList("a"));
        assertThat(!collection.isEmpty());
      }
    };
  }

  private static Test isEmptyReturnsTrueIfCollectionIsEmpty(final Class<?> type) {
    return new Case("isEmpty returns true if collection is empty") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(newArrayList());
        assertThat(collection.isEmpty());
      }
    };
  }
}