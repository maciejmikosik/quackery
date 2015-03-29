package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.quackery.Contract;

public class test_jdk_collections {
  private Contract<Class<?>> contract;

  public void array_list_quacks_like_mutable_list() {
    contract = quacksLike(List.class)
        .mutable();
    assertSuccess(contract.test(ArrayList.class));
  }

  public void linked_list_quacks_like_mutable_list() {
    contract = quacksLike(List.class)
        .mutable();
    assertSuccess(contract.test(LinkedList.class));
  }

  public void hash_set_quacks_like_mutable_collection() {
    contract = quacksLike(Collection.class)
        .mutable();
    assertSuccess(contract.test(HashSet.class));
  }

  public void tree_set_quacks_like_mutable_collection() {
    contract = quacksLike(Collection.class)
        .mutable();
    assertSuccess(contract.test(TreeSet.class));
  }
}
