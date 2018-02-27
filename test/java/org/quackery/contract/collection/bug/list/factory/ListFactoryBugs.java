package org.quackery.contract.collection.bug.list.factory;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class ListFactoryBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.list.factory.FactoryReturnsCollection.class));
}
