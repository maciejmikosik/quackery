package org.quackery.contract.collection.bug.list.mutable;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class ListMutableBugs {
  public static final List<? extends Class<?>> BUGS = unmodifiableList(asList(
      org.quackery.contract.collection.bug.list.mutable.AddAddsAtTheBegin.class,
      org.quackery.contract.collection.bug.list.mutable.AddReturnsFalse.class,
      org.quackery.contract.collection.bug.list.mutable.AddNotAddsDuplicatedElement.class,
      org.quackery.contract.collection.bug.list.mutable.SetAddsElement.class,
      org.quackery.contract.collection.bug.list.mutable.SetReturnsInsertedElement.class,
      org.quackery.contract.collection.bug.list.mutable.SetIndexesFromEnd.class,
      org.quackery.contract.collection.bug.list.mutable.AddIntAddsAtTheEnd.class));
}
