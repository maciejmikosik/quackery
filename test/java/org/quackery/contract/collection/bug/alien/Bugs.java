package org.quackery.contract.collection.bug.alien;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class Bugs {
  public static final List<? extends Class<?>> bugs = unmodifiableList(asList(
      Object.class,
      String.class,
      Integer.class,
      org.quackery.contract.collection.bug.alien.HasCollectionConstructors.class,
      org.quackery.contract.collection.bug.alien.FakeCollection.class));
}
