package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.collection.Factories.asListFactory;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.Collection;
import java.util.List;

public class test_passing_example_collections {
  public void mutable_list_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .mutable()
        .test(MutableList.class));
  }

  public void mutable_list_factory_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .mutable()
        .withFactory("create")
        .test(asListFactory(MutableList.class)));
  }

  public void immutable_list_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .test(ImmutableList.class));
  }

  public void immutable_list_factory_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .withFactory("create")
        .test(asListFactory(ImmutableList.class)));
  }

  public void mutable_list_forbidding_null_nicely_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .mutable()
        .test(MutableListForbiddingNullNicely.class));
  }

  public void mutable_list_factory_forbidding_null_nicely_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .mutable()
        .withFactory("create")
        .test(asListFactory(MutableListForbiddingNullNicely.class)));
  }

  public void mutable_list_forbidding_null_strictly_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .mutable()
        .test(MutableListForbiddingNullStrictly.class));
  }

  public void mutable_list_factory_forbidding_null_strictly_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .mutable()
        .withFactory("create")
        .test(asListFactory(MutableListForbiddingNullStrictly.class)));
  }
}
