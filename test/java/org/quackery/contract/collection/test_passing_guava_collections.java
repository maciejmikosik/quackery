package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class test_passing_guava_collections {
  private Contract<Class<?>> contract;

  public void immutable_list_quacks_like_immutable_list_with_factory() {
    contract = quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .withFactory("copyOf");
    assertSuccess(contract.test(ImmutableList.class));
  }

  public void immutable_set_quacks_like_immutable_collection_with_factory() {
    contract = quacksLike(Collection.class)
        .immutable()
        .withFactory("copyOf");
    assertSuccess(contract.test(ImmutableSet.class));
  }
}
