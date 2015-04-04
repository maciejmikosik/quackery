package org.quackery.contract.collection;

import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Flags.clean;
import static org.quackery.contract.collection.Flags.onlyIf;
import static org.quackery.contract.collection.suite.CollectionMutableSuite.clearRemovesElement;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorCreatesEmptyCollection;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorIsDeclared;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorIsDeclared;
import static org.quackery.contract.collection.suite.CollectionSuite.implementsCollectionInterface;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorCanCreateCollectionWithOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorDoesNotModifyArgument;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorFailsForNullArgument;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorMakesDefensiveCopy;
import static org.quackery.contract.collection.suite.CollectionSuite.isEmptyReturnsFalseIfCollectionHasOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.isEmptyReturnsTrueIfCollectionIsEmpty;
import static org.quackery.contract.collection.suite.CollectionSuite.sizeReturnsOneIfCollectionHasOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.sizeReturnsZeroIfCollectionIsEmpty;
import static org.quackery.contract.collection.suite.ListMutableSuite.addAddsElementAtTheEnd;
import static org.quackery.contract.collection.suite.ListMutableSuite.addReturnsTrue;
import static org.quackery.contract.collection.suite.ListSuite.getReturnsEachElement;
import static org.quackery.contract.collection.suite.ListSuite.getFailsForIndexAboveBound;
import static org.quackery.contract.collection.suite.ListSuite.getFailsForIndexBelowBound;
import static org.quackery.contract.collection.suite.ListSuite.instantiatorStoresAllElementsInOrder;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;
import org.quackery.Test;

public final class CollectionContract implements Contract<Class<?>> {
  private final Class<?> supertype;
  private final boolean mutable;

  private CollectionContract(Class<?> supertype, boolean mutable) {
    this.supertype = supertype;
    this.mutable = mutable;
  }

  public static CollectionContract collectionContract(Class<Collection> supertype,
      Collection<?>... erasure) {
    return new CollectionContract(supertype, false);
  }

  public static CollectionContract collectionContract(Class<List> supertype, List<?>... erasure) {
    return new CollectionContract(supertype, false);
  }

  public Test test(Class<?> type) {
    boolean isList = supertype == List.class;
    return clean(suite(name(type))
        .test(suite("quacks like Collection")
            .test(implementsCollectionInterface(type))
            .test(suite("provides default constructor")
                .test(defaultConstructorIsDeclared(type))
                .test(defaultConstructorCreatesEmptyCollection(type)))
            .test(suite("provides copy constructor")
                .test(instantiatorIsDeclared(type))
                .test(instantiatorCanCreateCollectionWithOneElement(type))
                .test(instantiatorFailsForNullArgument(type))
                .test(instantiatorMakesDefensiveCopy(type))
                .test(instantiatorDoesNotModifyArgument(type)))
            .test(suite("overrides size")
                .test(sizeReturnsZeroIfCollectionIsEmpty(type))
                .test(sizeReturnsOneIfCollectionHasOneElement(type)))
            .test(suite("overrides isEmpty")
                .test(isEmptyReturnsFalseIfCollectionHasOneElement(type))
                .test(isEmptyReturnsTrueIfCollectionIsEmpty(type))))
        .test(onlyIf(mutable, suite("quacks like mutable collection")
            .test(suite("overrides clear")
                .test(clearRemovesElement(type)))))
        .test(onlyIf(isList, suite("quacks like list")
            .test(suite("provides copy constructor")
                .test(instantiatorStoresAllElementsInOrder(type)))
            .test(suite("overrides get")
                .test(getReturnsEachElement(type))
                .test(getFailsForIndexAboveBound(type))
                .test(getFailsForIndexBelowBound(type)))))
        .test(onlyIf(isList && mutable, suite("quacks like mutable list")
            .test(suite("overrides add")
                .test(addAddsElementAtTheEnd(type))
                .test(addReturnsTrue(type))))));
  }

  private String name(Class<?> type) {
    StringBuilder builder = new StringBuilder();
    builder.append(type.getName() + " quacks like");
    if (mutable) {
      builder.append(" mutable");
    }
    builder.append(" ").append(supertype.getSimpleName().toLowerCase());
    return builder.toString();
  }

  public Contract<Class<?>> mutable() {
    return new CollectionContract(supertype, true);
  }
}
