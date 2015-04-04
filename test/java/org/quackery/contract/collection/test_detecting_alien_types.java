package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertFailure;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.quackery.Contract;

public class test_detecting_alien_types {
  private static final List<Class<?>> alienTypes = unmodifiableList(asList(
      Object.class,
      String.class,
      Integer.class,
      HasCollectionConstructors.class,
      FakeCollection.class));
  private Contract<Class<?>> contract;

  public void are_detected_by_collection_contract() {
    contract = quacksLike(Collection.class);
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }

  public void are_detected_by_mutable_collection_contract() {
    contract = quacksLike(Collection.class)
        .mutable();
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }

  public void are_detected_by_list_contract() {
    contract = quacksLike(List.class);
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }

  public void are_detected_by_mutable_list_contract() {
    contract = quacksLike(List.class)
        .mutable();
    for (Class<?> alienType : alienTypes) {
      assertFailure(contract.test(alienType));
    }
  }

  public static class HasCollectionConstructors {
    public HasCollectionConstructors() {}

    public HasCollectionConstructors(Collection<?> collection) {
      if (collection == null) {
        throw new NullPointerException();
      }
    }
  }

  public static class FakeCollection implements Collection {
    public FakeCollection() {}

    public FakeCollection(Collection<?> collection) {
      if (collection == null) {
        throw new NullPointerException();
      }
    }

    public int size() {
      return 0;
    }

    public boolean isEmpty() {
      return false;
    }

    public boolean contains(Object o) {
      return false;
    }

    public Iterator iterator() {
      return null;
    }

    public Object[] toArray() {
      return null;
    }

    public Object[] toArray(Object[] a) {
      return null;
    }

    public boolean add(Object e) {
      return false;
    }

    public boolean remove(Object o) {
      return false;
    }

    public boolean containsAll(Collection c) {
      return false;
    }

    public boolean addAll(Collection c) {
      return false;
    }

    public boolean removeAll(Collection c) {
      return false;
    }

    public boolean retainAll(Collection c) {
      return false;
    }

    public void clear() {}
  }
}
