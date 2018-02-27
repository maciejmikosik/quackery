package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class TestPassingGuavaCollections {
  private Contract<Class<?>> contract;

  public void immutable_list_passes() {
    contract = quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .forbidding(null)
        .withFactory("copyOf");
    assertSuccess(contract.test(ImmutableList.class));
  }

  public void immutable_set_passes() {
    contract = quacksLike(Collection.class)
        .immutable()
        .forbidding(null)
        .withFactory("copyOf");
    assertSuccess(contract.test(ImmutableSet.class));
  }
}
