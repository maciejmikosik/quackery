package org.quackery.contract.collection;

import static java.util.Arrays.asList;
import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.TestingContracts.assertFailure;
import static org.quackery.contract.TestingContracts.assertSuccess;
import static org.quackery.contract.collection.Factories.asFactory;
import static org.quackery.testing.Testing.assertTrue;
import static org.quackery.testing.Testing.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.quackery.QuackeryException;
import org.quackery.Test;
import org.quackery.contract.collection.correct.ImmutableList;
import org.quackery.contract.collection.correct.MutableList;
import org.quackery.contract.collection.correct.MutableListForbiddingNullNicely;
import org.quackery.contract.collection.correct.MutableListForbiddingNullStrictly;

import com.google.common.collect.ImmutableSet;

public class TestCollectionContract {
  public static void test_collection_contract() {
    detects_bugs();
    passes_example_collections();
    passes_jdk_collections();
    passes_guava_collections();
    root_suite_has_descriptive_name();
    validates_arguments();
  }

  private static void detects_bugs() {
    for (boolean immutable : asList(true, false)) {
      for (boolean forbiddingNull : asList(true, false)) {
        for (Class<?> implementing : asList(Collection.class, List.class)) {
          Bugs bugs = new Bugs();
          CollectionContract contract = quacksLike(Collection.class);
          if (immutable) {
            bugs = bugs.immutable();
            contract = contract.immutable();
          }
          if (forbiddingNull) {
            bugs = bugs.forbiddingNull();
            contract = contract.forbidding(null);
          }
          if (implementing == List.class) {
            bugs = bugs.list();
            contract = contract.implementing(List.class);
          }
          for (Class<?> bug : bugs.get()) {
            assertFailure(contract.test(bug));
          }
          for (Class<?> bug : bugs.factory("create").get()) {
            assertFailure(contract.withFactory("create").test(bug));
          }
        }
      }
    }
  }

  private static void passes_example_collections() {
    String factoryName = "create";
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .test(MutableList.class));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, MutableList.class)));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .test(ImmutableList.class));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, ImmutableList.class)));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .test(MutableListForbiddingNullNicely.class));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, MutableListForbiddingNullNicely.class)));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .test(MutableListForbiddingNullStrictly.class));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, MutableListForbiddingNullStrictly.class)));
  }

  private static void passes_jdk_collections() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .test(ArrayList.class));
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .test(LinkedList.class));
    assertSuccess(quacksLike(Collection.class)
        .test(HashSet.class));
    assertSuccess(quacksLike(Collection.class)
        .forbidding(null)
        .test(TreeSet.class));
  }

  private static void passes_guava_collections() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .forbidding(null)
        .withFactory("copyOf")
        .test(com.google.common.collect.ImmutableList.class));
    assertSuccess(quacksLike(Collection.class)
        .immutable()
        .forbidding(null)
        .withFactory("copyOf")
        .test(ImmutableSet.class));
  }

  private static void root_suite_has_descriptive_name() {
    class SomeClass {}
    assertNameContains(SomeClass.class.getSimpleName(),
        quacksLike(Collection.class)
            .test(SomeClass.class));
    assertNameContains(" collection",
        quacksLike(Collection.class)
            .test(Object.class));
    assertNameContains(" immutable",
        quacksLike(Collection.class)
            .immutable()
            .test(Object.class));
    assertNameContains(" forbidding null",
        quacksLike(Collection.class)
            .forbidding(null)
            .test(Object.class));
    assertNameContains(" with factory method_name",
        quacksLike(Collection.class)
            .withFactory("method_name")
            .test(Object.class));
    assertNameContains(" list",
        quacksLike(Collection.class)
            .implementing(List.class)
            .test(Object.class));
  }

  private static void assertNameContains(String string, Test test) {
    assertTrue(test.name.contains(string));
  }

  private static void validates_arguments() {
    try {
      quacksLike(Collection.class)
          .implementing(Object.class);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .implementing(Collection.class);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .implementing(Set.class);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .implementing(Map.class);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .implementing(List.class)
          .implementing(List.class);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .implementing(null);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .immutable()
          .immutable();
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .forbidding(null)
          .forbidding(null);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .withFactory("create")
          .withFactory("create");
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .withFactory(null);
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .test(null);
      fail();
    } catch (QuackeryException e) {}
  }
}
