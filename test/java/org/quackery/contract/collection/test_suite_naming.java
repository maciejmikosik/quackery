package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.quackery.Test;

public class test_suite_naming {
  private Test test;

  public void includes_class_name() {
    class SomeClass {}
    test = quacksLike(Collection.class)
        .test(SomeClass.class);
    assertTrue(test.name.contains(SomeClass.class.getSimpleName()));
  }

  public void includes_collection() {
    test = quacksLike(Collection.class)
        .test(Object.class);
    assertTrue(test.name.contains(" collection"));
  }

  public void includes_mutable() {
    test = quacksLike(Collection.class)
        .mutable()
        .test(Object.class);
    assertTrue(test.name.contains(" mutable"));
  }

  public void includes_immutable() {
    test = quacksLike(Collection.class)
        .immutable()
        .test(Object.class);
    assertTrue(test.name.contains(" immutable"));
  }

  public void includes_forbidding_null() {
    test = quacksLike(Collection.class)
        .forbidding(null)
        .test(Object.class);
    assertTrue(test.name.contains(" forbidding null"));
  }

  public void includes_factory() {
    test = quacksLike(Collection.class)
        .withFactory("method_name")
        .test(Object.class);
    assertTrue(test.name.contains(" factory"));
    assertTrue(test.name.contains(" method_name"));
  }

  public void includes_list() {
    test = quacksLike(Collection.class)
        .implementing(List.class)
        .test(Object.class);
    assertTrue(test.name.contains(" list"));
  }
}
