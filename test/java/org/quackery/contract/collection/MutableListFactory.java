package org.quackery.contract.collection;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.Bug;

public class MutableListFactory {
  public static <E> List<E> create(Collection<? extends E> collection) {
    return new MutableList(collection);
  }

  // no constructors
  private MutableListFactory(Object o) {}

  @Bug(Collection.class)
  public static class FactoryIsMissing {}

  @Bug(Collection.class)
  public static class FactoryIsHidden {
    static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryIsNotStatic {
    public <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryReturnsObject {
    public static <E> Object create(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryHasDifferentName {
    public static <E> List<E> differentName(Collection<? extends E> collection) {
      return new MutableList(collection);
    }
  }

  @Bug(Collection.class)
  public static class FactoryAcceptsObject {
    public static <E> List<E> create(Object collection) {
      return new MutableList((Collection) collection);
    }
  }
}
