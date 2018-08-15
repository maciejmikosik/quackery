package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.TestingContracts.assertSuccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.quackery.Contract;

public class TestPassingJdkCollections {
  private Contract<Class<?>> contract;

  public void array_list_passes() {
    contract = quacksLike(Collection.class)
        .implementing(List.class);
    assertSuccess(contract.test(ArrayList.class));
  }

  public void linked_list_passes() {
    contract = quacksLike(Collection.class)
        .implementing(List.class);
    assertSuccess(contract.test(LinkedList.class));
  }

  public void hash_set_passes() {
    contract = quacksLike(Collection.class);
    assertSuccess(contract.test(HashSet.class));
  }

  public void tree_set_passes() {
    contract = quacksLike(Collection.class)
        .forbidding(null);
    assertSuccess(contract.test(TreeSet.class));
  }
}
