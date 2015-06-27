package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.List;

public class test_example_collections {
  public void mutable_list_quacks_like_mutable_list() {
    assertSuccess(quacksLike(List.class)
        .test(MutableList.class));
  }

  public void mutable_list_factory_quacks_list_mutable_list_factory() {
    assertSuccess(quacksLike(List.class)
        .withFactory("create")
        .test(MutableListFactory.class));
  }
}
