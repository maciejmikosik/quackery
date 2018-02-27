package org.quackery.contract.collection.bug.collection.forbiddingNull;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class CollectionForbiddingNullBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.collection.forbiddingNull.CreatorAllowsNullElements.class));
}
