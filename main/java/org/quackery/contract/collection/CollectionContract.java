package org.quackery.contract.collection;

import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Flags.clean;
import static org.quackery.contract.collection.Flags.onlyIf;
import static org.quackery.contract.collection.suite.CollectionMutableSuite.clearRemovesElement;
import static org.quackery.contract.collection.suite.CollectionSuite.copyConstructorIsDeclared;
import static org.quackery.contract.collection.suite.CollectionSuite.copyConstructorIsPublic;
import static org.quackery.contract.collection.suite.CollectionSuite.creatorCanCreateCollectionWithOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.creatorDoesNotModifyArgument;
import static org.quackery.contract.collection.suite.CollectionSuite.creatorFailsForNullArgument;
import static org.quackery.contract.collection.suite.CollectionSuite.creatorMakesDefensiveCopy;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorCreatesEmptyCollection;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorIsDeclared;
import static org.quackery.contract.collection.suite.CollectionSuite.defaultConstructorIsPublic;
import static org.quackery.contract.collection.suite.CollectionSuite.factoryIsDeclared;
import static org.quackery.contract.collection.suite.CollectionSuite.factoryIsPublic;
import static org.quackery.contract.collection.suite.CollectionSuite.factoryIsStatic;
import static org.quackery.contract.collection.suite.CollectionSuite.factoryReturnsCollection;
import static org.quackery.contract.collection.suite.CollectionSuite.implementsCollectionInterface;
import static org.quackery.contract.collection.suite.CollectionSuite.isEmptyReturnsFalseIfCollectionHasOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.isEmptyReturnsTrueIfCollectionIsEmpty;
import static org.quackery.contract.collection.suite.CollectionSuite.sizeReturnsOneIfCollectionHasOneElement;
import static org.quackery.contract.collection.suite.CollectionSuite.sizeReturnsZeroIfCollectionIsEmpty;
import static org.quackery.contract.collection.suite.ListMutableSuite.addAddsElementAtTheEnd;
import static org.quackery.contract.collection.suite.ListMutableSuite.addReturnsTrue;
import static org.quackery.contract.collection.suite.ListSuite.cretorStoresAllElementsInOrder;
import static org.quackery.contract.collection.suite.ListSuite.getFailsForIndexAboveBound;
import static org.quackery.contract.collection.suite.ListSuite.getFailsForIndexBelowBound;
import static org.quackery.contract.collection.suite.ListSuite.getReturnsEachElement;

import java.util.Collection;
import java.util.List;

import org.quackery.Contract;
import org.quackery.Test;

public final class CollectionContract implements Contract<Class<?>> {
  private final Class<?> supertype;
  private final boolean mutable;
  private final String factory;

  private CollectionContract(Class<?> supertype) {
    this.supertype = supertype;
    mutable = false;
    factory = null;
  }

  private CollectionContract(Class<?> supertype, boolean mutable, String factory) {
    this.supertype = supertype;
    this.mutable = mutable;
    this.factory = factory;
  }

  public static CollectionContract collectionContract(Class<Collection> supertype,
      Collection<?>... erasure) {
    return new CollectionContract(supertype);
  }

  public static CollectionContract collectionContract(Class<List> supertype, List<?>... erasure) {
    return new CollectionContract(supertype);
  }

  public Test test(Class<?> type) {
    boolean isList = supertype == List.class;
    boolean hasConstructor = factory == null;
    boolean hasFactory = factory != null;
    Creator creator = hasConstructor
        ? new ConstructorCreator(type)
        : new FactoryCreator(type, factory);
    return clean(suite(name(type))
        .test(suite("quacks like Collection")
            .test(onlyIf(hasConstructor, implementsCollectionInterface(type)))
            .test(onlyIf(hasConstructor, suite("provides default constructor")
                .test(defaultConstructorIsDeclared(type))
                .test(defaultConstructorIsPublic(type))
                .test(defaultConstructorCreatesEmptyCollection(type))))
            .test(suite("provides " + name(creator))
                .test(onlyIf(hasConstructor, copyConstructorIsDeclared(type)))
                .test(onlyIf(hasConstructor, copyConstructorIsPublic(type)))
                .test(onlyIf(hasFactory, factoryIsDeclared(type, factory)))
                .test(onlyIf(hasFactory, factoryIsPublic(type, factory)))
                .test(onlyIf(hasFactory, factoryIsStatic(type, factory)))
                .test(onlyIf(hasFactory, factoryReturnsCollection(type, factory)))
                .test(creatorCanCreateCollectionWithOneElement(creator))
                .test(creatorFailsForNullArgument(creator))
                .test(creatorMakesDefensiveCopy(creator))
                .test(creatorDoesNotModifyArgument(creator)))
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
            .test(suite("provides " + name(creator))
                .test(cretorStoresAllElementsInOrder(creator)))
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

  private static String name(Creator creator) {
    return creator instanceof ConstructorCreator
        ? "copy constructor"
        : "factory method";
  }

  public CollectionContract mutable() {
    return new CollectionContract(supertype, true, factory);
  }

  public CollectionContract withFactory(String factoryMethodName) {
    return new CollectionContract(supertype, mutable, factoryMethodName);
  }
}
