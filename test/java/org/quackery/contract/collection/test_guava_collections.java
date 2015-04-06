package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.quackery.Contract;
import org.quackery.QuackeryRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

@RunWith(QuackeryRunner.class)
public class test_guava_collections {
  private Contract<Class<?>> contract;

  public void immutable_list_quacks_like_list() {
    contract = quacksLike(List.class)
        .withFactory("copyOf");
    assertSuccess(contract.test(ImmutableList.class));
  }

  public void immutable_set_quacks_like_collection() {
    contract = quacksLike(Collection.class)
        .withFactory("copyOf");
    assertSuccess(contract.test(ImmutableSet.class));
  }
}