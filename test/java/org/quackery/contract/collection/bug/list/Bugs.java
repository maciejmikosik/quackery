package org.quackery.contract.collection.bug.list;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class Bugs {
  public static final List<? extends Class<?>> bugs = unmodifiableList(asList(
      org.quackery.contract.collection.bug.list.CopyConstructorStoresOneElement.class,
      org.quackery.contract.collection.bug.list.CopyConstructorReversesOrder.class,
      org.quackery.contract.collection.bug.list.CopyConstructorRemovesLastElement.class,
      org.quackery.contract.collection.bug.list.CopyConstructorRemovesDuplicates.class,
      org.quackery.contract.collection.bug.list.GetReturnsFirstElement.class,
      org.quackery.contract.collection.bug.list.GetReturnsLastElement.class,
      org.quackery.contract.collection.bug.list.GetReturnsNull.class,
      org.quackery.contract.collection.bug.list.GetReturnsNullAboveBound.class,
      org.quackery.contract.collection.bug.list.GetReturnsNullBelowBound.class));
}
