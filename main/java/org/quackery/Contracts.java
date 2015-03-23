package org.quackery;

import static java.util.Arrays.asList;
import static org.quackery.Suite.newSuite;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Contracts {
  public static Contract<Class<?>> quacksLike(
      @SuppressWarnings("rawtypes") Class<Collection> collectionType) {
    return new Contract<Class<?>>() {
      public Test test(Class<?> type) {
        return newSuite(type.getName() + " quacks like " + Collection.class.getName()) //
            .test(implementsCollection(type)) //
            .test(newSuite("implements default constructor") //
                .test(hasDefaultConstructor(type)) //
                .test(defaultConstructorCreatesEmptyCollection(type))) //
            .test(newSuite("implements copy constructor") //
                .test(hasCopyConstructor(type)) //
                .test(copyConstructorCanCreateCollectionWithOneElement(type)) //
                .test(copyConstructorFailsForNullArgument(type)) //
                .test(copyConstructorMakesDefensiveCopy(type)) //
                .test(copyConstructorDoesNotModifyArgument(type)));
      }
    };
  }

  private static Test implementsCollection(final Class<?> type) {
    return new Case("implements Collection") {
      public void run() throws Throwable {
        boolean expected = Collection.class.isAssignableFrom(type);
        if (!expected) {
          throw new FailureException("" //
              + "\n" //
              + "  expected that\n" //
              + "    " + type.getName() + "\n" //
              + "  implements\n" //
              + "  " + Collection.class.getName() + "\n" //
          );
        }
      }
    };
  }

  private static Test hasDefaultConstructor(final Class<?> type) {
    return new Case("has default constructor") {
      public void run() {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
          if (isPublic(constructor) && parameters(constructor).isEmpty()) {
            return;
          }
        }
        throw new FailureException("" //
            + "\n" //
            + "  expected that\n" //
            + "    " + type.getName() + "\n" //
            + "  has public constructor with no parameters\n" //
        );
      }
    };
  }

  private static Case defaultConstructorCreatesEmptyCollection(final Class<?> type) {
    return new Case("default constructor creates empty collection") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        Collection<?> collection = (Collection<?>) assumeConstructor(type).newInstance();
        Object[] toArray = collection.toArray();
        boolean expected = Arrays.equals(toArray, new Object[0]);
        if (!expected) {
          throw new FailureException("" //
              + "\n" //
              + "  Expected that\n" //
              + "    " + type + "\n" //
              + "  has default constructor that creates empty collection, \n" //
              + "  but collection contained elements\n" //
              + "    " + print(toArray) + "\n" //
          );
        }
      }
    };
  }

  private static Test hasCopyConstructor(final Class<?> type) {
    return new Case("has copy constructor") {
      public void run() {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
          if (isPublic(constructor) && parameters(constructor).equals(asList(Collection.class))) {
            return;
          }
        }
        throw new FailureException("" //
            + "\n" //
            + "  expected that\n" //
            + "    " + type.getName() + "\n" //
            + "  has public constructor with parameter\n" //
            + "    " + Collection.class.getName() + "\n" //
        );
      }
    };
  }

  private static Test copyConstructorCanCreateCollectionWithOneElement(final Class<?> type) {
    return new Case("copy constructor can create collection with one element") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        ArrayList<Object> original = new ArrayList<Object>(asList("a"));
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(original.clone());
        Object[] toArray = collection.toArray();
        boolean expected = Arrays.equals(toArray, original.toArray());
        if (!expected) {
          throw new FailureException("" //
              + "\n" //
              + "  Expected that\n" //
              + "    " + type.getName() + "\n" //
              + "  has copy constructor that accepts collection with one element.\n" //
              + "  If collection passed as an argument contains\n" //
              + "    " + print(original) + "\n" //
              + "  then created collection contains\n" //
              + "    " + print(toArray) + "\n" //
              + "  It should contain same elements.\n" //
          );
        }
      }
    };
  }

  private static Test copyConstructorFailsForNullArgument(final Class<?> type) {
    return new Case("copy constructor fails for null argument") {
      public void run() throws Throwable {
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = null;
        try {
          collection = (Collection<?>) constructor.newInstance((Object) null);
          throw new FailureException("" //
              + "\n" //
              + "  Expected that\n" //
              + "    " + type + "\n" //
              + "  has copy constructor that throws NullPointerException for null argument.\n" //
              + "  Instead it created collection containing\n" //
              + "    " + print(collection) + "\n" //
          );
        } catch (InvocationTargetException e) {
          if (!(e.getCause() instanceof NullPointerException)) {
            throw new FailureException("" //
                + "\n" //
                + "  Expected that\n" //
                + "    " + type + "\n" //
                + "  has copy constructor that throws NullPointerException for null argument.\n" //
                + "  Instead it thrown " + e.getCause().getClass().getName() + "\n" //
            );
          }
        }
      }
    };
  }

  private static Test copyConstructorMakesDefensiveCopy(final Class<?> type) {
    return new Case("copy constructor makes defensive copy") {
      public void run() throws Throwable {
        assume(Collection.class.isAssignableFrom(type));
        ArrayList<Object> original = new ArrayList<Object>(asList("a", "b", "c"));
        ArrayList<Object> trojan = (ArrayList<Object>) original.clone();
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        Collection<?> collection = (Collection<?>) constructor.newInstance(trojan);
        Object[] beforeToArray = collection.toArray();
        trojan.remove(1);
        Object[] afterToArray = collection.toArray();
        boolean expected = Arrays.equals(beforeToArray, afterToArray);
        if (!expected) {
          throw new FailureException("" //
              + "\n" //
              + "  Expected that\n" //
              + "    " + type + "\n" //
              + "  has copy constructor that defensive copies elements.\n" //
              + "\n" //
              + "  If collection passed as an argument to copy constructor contains\n" //
              + "    " + print(original) + "\n" //
              + "  then created collection contains\n" //
              + "    " + print(beforeToArray) + "\n" //
              + "  If you change collection passed as argument to\n" //
              + "    " + print(trojan) + "\n" //
              + "  as a result, created collection changes to\n" //
              + "    " + print(afterToArray) + "\n" //
              + "  It should remain unchanged.\n" //
          );
        }
      }
    };
  }

  private static Test copyConstructorDoesNotModifyArgument(final Class<?> type) {
    return new Case("copy constructor does not modify argument") {
      public void run() throws Throwable {
        ArrayList<Object> original = new ArrayList<Object>(asList("a", "b", "c"));
        ArrayList<Object> argument = (ArrayList<Object>) original.clone();
        Constructor<?> constructor = assumeConstructor(type, Collection.class);
        constructor.newInstance(argument);

        Object[] argumentToArray = argument.toArray();
        boolean expected = Arrays.equals(argumentToArray, original.toArray());
        if (!expected) {
          throw new FailureException("" //
              + "\n" //
              + "  Expected that\n" //
              + "    " + type + "\n" //
              + "  has copy constructor that does not modify argument.\n" //
              + "\n" //
              + "  However if collection passed as an argument to copy constructor contains\n" //
              + "    " + print(original) + "\n" //
              + "  after constructor finishes it is changed to\n" //
              + "    " + print(argumentToArray) + "\n" //
              + "  It should remain unchanged.\n" //
          );
        }
      }
    };
  }

  private static void assume(boolean condition) {
    if (!condition) {
      throw new AssumptionException();
    }
  }

  private static Constructor<?> assumeConstructor(Class<?> type, Class<?>... parameters) {
    try {
      return type.getConstructor(parameters);
    } catch (NoSuchMethodException e) {
      throw new AssumptionException(e);
    }
  }

  private static List<Class<?>> parameters(Constructor<?> constructor) {
    return asList(constructor.getParameterTypes());
  }

  private static String print(Object[] array) {
    return Arrays.toString(array);
  }

  private static String print(Collection<?> collection) {
    return print(collection.toArray());
  }

  private static boolean isPublic(Constructor<?> constructor) {
    return Modifier.isPublic(constructor.getModifiers());
  }
}
