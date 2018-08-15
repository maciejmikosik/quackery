package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.TestingContracts.assertSuccess;
import static org.quackery.contract.collection.Factories.asFactory;

import java.util.Collection;
import java.util.List;

import org.quackery.contract.collection.correct.ImmutableList;
import org.quackery.contract.collection.correct.MutableList;
import org.quackery.contract.collection.correct.MutableListForbiddingNullNicely;
import org.quackery.contract.collection.correct.MutableListForbiddingNullStrictly;

public class TestPassingExampleCollections {
  private static final String factoryName = "create";

  public void mutable_list_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .test(MutableList.class));
  }

  public void mutable_list_factory_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, MutableList.class)));
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
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, ImmutableList.class)));
  }

  public void mutable_list_forbidding_null_nicely_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .test(MutableListForbiddingNullNicely.class));
  }

  public void mutable_list_factory_forbidding_null_nicely_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, MutableListForbiddingNullNicely.class)));
  }

  public void mutable_list_forbidding_null_strictly_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .test(MutableListForbiddingNullStrictly.class));
  }

  public void mutable_list_factory_forbidding_null_strictly_passes() {
    assertSuccess(quacksLike(Collection.class)
        .implementing(List.class)
        .forbidding(null)
        .withFactory(factoryName)
        .test(asFactory(List.class, factoryName, MutableListForbiddingNullStrictly.class)));
  }
}
