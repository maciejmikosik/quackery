package org.quackery.contract.collection.bug.collection.immutable;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class CollectionImmutableBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.collection.immutable.IteratorRemovesElement.class,
      org.quackery.contract.collection.bug.collection.immutable.IteratorDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.AddAddsElement.class,
      org.quackery.contract.collection.bug.collection.immutable.AddDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveRemovesElement.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.AddAllAddsElements.class,
      org.quackery.contract.collection.bug.collection.immutable.AddAllDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveAllRemovesElements.class,
      org.quackery.contract.collection.bug.collection.immutable.RemoveAllDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.RetainAllRetainsElements.class,
      org.quackery.contract.collection.bug.collection.immutable.RetainAllDoesNotThrowException.class,
      org.quackery.contract.collection.bug.collection.immutable.ClearClearsElements.class,
      org.quackery.contract.collection.bug.collection.immutable.ClearDoesNotThrowException.class));
}
