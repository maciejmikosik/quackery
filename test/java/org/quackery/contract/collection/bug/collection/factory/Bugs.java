package org.quackery.contract.collection.bug.collection.factory;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class Bugs {
  public static final List<? extends Class<?>> bugs = unmodifiableList(asList(
      org.quackery.contract.collection.bug.collection.factory.FactoryIsMissing.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryIsHidden.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryIsNotStatic.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryReturnsObject.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryHasDifferentName.class,
      org.quackery.contract.collection.bug.collection.factory.FactoryAcceptsObject.class));
}
