package org.quackery.contract.collection;

import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Flags.clean;
import static org.quackery.contract.collection.Flags.onlyIf;
import static org.quackery.contract.collection.suite.CollectionMutableSuite.clearRemovesElement;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorCreatesEmptyCollection;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorIsDeclared;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorIsPublic;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorCanCreateCollectionWithOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorDoesNotModifyArgument;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorFailsForNullArgument;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorIsDeclared;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorIsPublic;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorMakesDefensiveCopy;
import static org.quackery.contract.collection.suite.CollectionSuite.instantiatorReturnsCollection;
import static org.quackery.contract.collection.suite.CollectionSuite.isEmptyReturnsFalseIfCollectionHasOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.isEmptyReturnsTrueIfCollectionIsEmpty;
import static org.quackery.contract.collection.suite.CollectionSuite.sizeReturnsOneIfCollectionHasOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.sizeReturnsZeroIfCollectionIsEmpty;
import static org.quackery.contract.collection.suite.ListMutableSuite.addAddsElementAtTheEnd;
import static org.quackery.contract.collection.suite.ListMutableSuite.addReturnsTrue;
import static org.quackery.contract.collection.suite.ListSuite.getFailsForIndexAboveBound;
import static org.quackery.contract.collection.suite.ListSuite.getFailsForIndexBelowBound;
import static org.quackery.contract.collection.suite.ListSuite.getReturnsEachElement;
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
    Creator creator = new ConstructorCreator(type);
    return clean(suite(name(type))
        .test(suite("quacks like Collection")
            .test(suite("provides default constructor")
                .test(defaultConstructorIsDeclared(type))
                .test(defaultConstructorIsPublic(type))
                .test(defaultConstructorCreatesEmptyCollection(type)))
            .test(suite("provides copy constructor")
                .test(instantiatorIsDeclared(type))
                .test(instantiatorIsPublic(type))
                .test(instantiatorReturnsCollection(type))
                .test(instantiatorCanCreateCollectionWithOneElement(creator))
                .test(instantiatorFailsForNullArgument(creator))
                .test(instantiatorMakesDefensiveCopy(creator))
                .test(instantiatorDoesNotModifyArgument(creator)))
            .test(suite("overrides size")
                .test(sizeReturnsZeroIfCollectionIsEmpty(creator))
                .test(sizeReturnsOneIfCollectionHasOneElement(creator)))
            .test(suite("overrides isEmpty")
                .test(isEmptyReturnsFalseIfCollectionHasOneElement(creator))
                .test(isEmptyReturnsTrueIfCollectionIsEmpty(creator))))
        .test(onlyIf(mutable, suite("quacks like mutable collection")
            .test(suite("overrides clear")
                .test(clearRemovesElement(creator)))))
        .test(onlyIf(isList, suite("quacks like list")
            .test(suite("provides copy constructor")
                .test(instantiatorStoresAllElementsInOrder(creator)))
            .test(suite("overrides get")
                .test(getReturnsEachElement(creator))
                .test(getFailsForIndexAboveBound(creator))
                .test(getFailsForIndexBelowBound(creator)))))
        .test(onlyIf(isList && mutable, suite("quacks like mutable list")
            .test(suite("overrides add")
                .test(addAddsElementAtTheEnd(creator))
                .test(addReturnsTrue(creator))))));
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
