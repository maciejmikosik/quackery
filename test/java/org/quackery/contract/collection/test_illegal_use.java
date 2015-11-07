package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.fail;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quackery.QuackeryException;

public class test_illegal_use {
  public void cannot_implement_object() {
    try {
      quacksLike(Collection.class)
          .implementing(Object.class);
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_implement_collection() {
    try {
      quacksLike(Collection.class)
          .implementing(Collection.class);
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_implement_set() {
    try {
      quacksLike(Collection.class)
          .implementing(Set.class);
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_implement_map() {
    try {
      quacksLike(Collection.class)
          .implementing(Map.class);
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_implement_same_interface_twice() {
    try {
      quacksLike(Collection.class)
          .implementing(List.class)
          .implementing(List.class);
      fail();
    } catch (QuackeryException e) {}
  }

  public void implemented_interface_cannot_be_null() {
    try {
      quacksLike(Collection.class)
          .implementing(null);
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_declare_mutable_twice() {
    try {
      quacksLike(Collection.class)
          .mutable()
          .mutable();
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_declare_immutable_twice() {
    try {
      quacksLike(Collection.class)
          .immutable()
          .immutable();
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_declare_forbidding_twice() {
    try {
      quacksLike(Collection.class)
          .forbidding(null)
          .forbidding(null);
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_mix_mutable_and_immutable() {
    try {
      quacksLike(Collection.class)
          .mutable()
          .immutable();
      fail();
    } catch (QuackeryException e) {}
    try {
      quacksLike(Collection.class)
          .immutable()
          .mutable();
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_declare_factory_twice() {
    try {
      quacksLike(Collection.class)
          .withFactory("create")
          .withFactory("create");
      fail();
    } catch (QuackeryException e) {}
  }

  public void factory_name_cannot_be_null() {
    try {
      quacksLike(Collection.class)
          .withFactory(null);
      fail();
    } catch (QuackeryException e) {}
  }

  public void cannot_test_null() {
    try {
      quacksLike(Collection.class)
          .test(null);
      fail();
    } catch (QuackeryException e) {}
  }
}
