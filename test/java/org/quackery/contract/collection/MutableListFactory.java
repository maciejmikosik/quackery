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

  @Bug({ List.class, Mutable.class })
  public static class AddHasNoEffect {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.AddHasNoEffect(collection);
    }
  }

  @Bug({ List.class, Mutable.class })
  public static class AddAddsAtTheBegin {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.AddAddsAtTheBegin(collection);
    }
  }

  @Bug({ List.class, Mutable.class })
  public static class AddReturnsFalse {
    public static <E> List<E> create(Collection<? extends E> collection) {
      return new MutableList.AddReturnsFalse(collection);
    }
  }
}
