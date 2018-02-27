package org.quackery.contract.collection.bug.list.immutable;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class ListImmutableBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.list.immutable.AddAllIntAddsElements.class,
      org.quackery.contract.collection.bug.list.immutable.AddAllIntDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.SetSetsElement.class,
      org.quackery.contract.collection.bug.list.immutable.SetDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.AddIntAddsElement.class,
      org.quackery.contract.collection.bug.list.immutable.AddIntDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.RemoveIntRemovesElement.class,
      org.quackery.contract.collection.bug.list.immutable.RemoveIntDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorRemovesElement.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorRemoveDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorSetsElement.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorSetDoesNotThrowException.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorAddsElement.class,
      org.quackery.contract.collection.bug.list.immutable.ListIteratorAddDoesNotThrowException.class));
}
