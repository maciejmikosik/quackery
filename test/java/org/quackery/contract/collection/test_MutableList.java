package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;

public class test_MutableList {
  private final Class<?> model = MutableList.class;
  private Contract<Class<?>> contract;

  public void quacks_like_collection() {
    contract = quacksLike(Collection.class);
    assertSuccess(contract.test(model));
  }

  public void quacks_like_mutable_collection() {
    contract = quacksLike(Collection.class).mutable();
    assertSuccess(contract.test(model));
  }

  public void quacks_like_list() {
    contract = quacksLike(List.class);
    assertSuccess(contract.test(model));
  }

  public void quacks_like_mutable_list() {
    contract = quacksLike(List.class).mutable();
    assertSuccess(contract.test(model));
  }
}
