package org.quackery.contract.collection.bug.collection.constructor;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class CollectionConstructorBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorIsMissing.class,
      org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorIsHidden.class,
      org.quackery.contract.collection.bug.collection.constructor.DefaultConstructorAddsElement.class,
      org.quackery.contract.collection.bug.collection.constructor.CopyConstructorIsMissing.class,
      org.quackery.contract.collection.bug.collection.constructor.CopyConstructorIsHidden.class));
}
