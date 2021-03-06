package org.quackery.contract.collection.bug.collection.mutableForbiddingNull;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class CollectionMutableForbiddingNullBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.collection.mutableForbiddingNull.AddAllowsNullElements.class));
}
