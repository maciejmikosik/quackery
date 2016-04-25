package org.quackery.contract.collection.bug.list.factory;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class Bugs {
  public static final List<? extends Class<?>> bugs = unmodifiableList(asList(
      org.quackery.contract.collection.bug.list.factory.FactoryReturnsCollection.class));
}
