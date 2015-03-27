package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;

import java.util.Collection;

import org.quackery.Contract;

public class test_quacksLike_collection_mutable {
  @SuppressWarnings("unused")
  private final Contract<Class<?>> contract = quacksLike(Collection.class).mutable();

  public void placeholder() {}
}
