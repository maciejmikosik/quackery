package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;
import java.util.List;

public class test_example_collections {
  public void mutable_list_quacks_like_mutable_list() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .test(MutableList.class));
  }

  public void mutable_list_factory_quacks_list_mutable_list_factory() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .withFactory("create")
        .test(asListFactory(MutableList.class)));
  }
}