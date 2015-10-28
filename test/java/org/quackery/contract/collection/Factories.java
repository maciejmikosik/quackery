package org.quackery.contract.collection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class Factories {
  public static Class<?> asCollectionFactory(Class<?> original) {
    return as(CollectionFactory.class, original);
  }

  public static Class<?> asListFactory(Class<?> original) {
    return as(ListFactory.class, original);
  }

  private static Class<?> as(Class<?> prototype, Class<?> original) {
    try {
      Class<?> clone = clone(prototype);
      clone.getField("original").set(null, original);
      return clone;
    } catch (ReflectiveOperationException e) {
      throw new LinkageError("", e);
    }
  }

  public static class CollectionFactory {
    public static Class<?> original;

    public static Collection<?> create(Collection<?> collection) throws Throwable {
      try {
        return (Collection<?>) original.getConstructor(Collection.class).newInstance(collection);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      } catch (ReflectiveOperationException e) {
        throw new LinkageError("", e);
      }
    }
  }

  public static class ListFactory {
    public static Class<?> original;

    public static List<?> create(Collection<?> collection) throws Throwable {
      try {
        return (List<?>) original.getConstructor(Collection.class).newInstance(collection);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      } catch (ReflectiveOperationException e) {
        throw new LinkageError("", e);
      }
    }
  }

  private static Class<?> clone(final Class<?> type) {
    ClassLoader classLoader = new ClassLoader(null) {
      public Class findClass(String name) throws ClassNotFoundException {
        byte[] bytecode = bytecode(type);
        return name.equals(type.getName())
            ? defineClass(name, bytecode, 0, bytecode.length)
            : super.findClass(name);
      }
    };
    try {
      return classLoader.loadClass(type.getName());
    } catch (ClassNotFoundException e) {
      throw new LinkageError("", e);
    }
  }

  private static byte[] bytecode(Class<?> type) {
    try {
      InputStream input = type.getResourceAsStream(classFileName(type));
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy(input, output);
      input.close();
      return output.toByteArray();
    } catch (IOException e) {
      throw new LinkageError("", e);
    }
  }

  private static String classFileName(Class<?> type) {
    String[] tokens = type.getName().split("\\.");
    return tokens[tokens.length - 1] + ".class";
  }

  private static void copy(InputStream input, OutputStream output) throws IOException {
    int read;
    while ((read = input.read()) != -1) {
      output.write(read);
    }
  }
}
