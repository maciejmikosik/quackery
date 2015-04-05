package org.quackery.contract.collection;

import static org.quackery.Contracts.quacksLike;
import static org.quackery.contract.Bugs.bugs;
import static org.quackery.testing.Assertions.assertFailure;
import static org.quackery.testing.Assertions.assertSuccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quackery.Contract;

public class test_MutableList {
  private final Class<?> model = MutableList.class;
  private Contract<Class<?>> contract;

  public void quacks_like_collection() {
    contract = quacksLike(Collection.class);
    assertSuccess(contract.test(model));
    for (Class<?> bug : bugs(model, Collection.class)) {
      assertFailure(contract.test(bug));
    }
  }

  public void quacks_like_mutable_collection() {
    contract = quacksLike(Collection.class).mutable();
    assertSuccess(contract.test(model));
    for (Class<?> bug : join(
        bugs(model, Collection.class),
        bugs(model, Collection.class, Mutable.class))) {
      assertFailure(contract.test(bug));
    }
  }

  public void quacks_like_list() {
    contract = quacksLike(List.class);
    assertSuccess(contract.test(model));
    for (Class<?> bug : join(
        bugs(model, Collection.class),
        bugs(model, List.class))) {
      assertFailure(contract.test(bug));
    }
  }

  public void quacks_like_mutable_list() {
    contract = quacksLike(List.class).mutable();
    assertSuccess(contract.test(model));
    for (Class<?> bug : join(
        bugs(model, Collection.class),
        bugs(model, Collection.class, Mutable.class),
        bugs(model, List.class),
        bugs(model, List.class, Mutable.class))) {
      assertFailure(contract.test(bug));
    }
  }

  private static List<Class<?>> join(List<Class<?>>... parts) {
    List<Class<?>> joined = new ArrayList<>();
    for (List<Class<?>> part : parts) {
      joined.addAll(part);
    }
    return joined;
  }
}
