package org.quackery.contract.collection;

import static java.util.Collections.unmodifiableList;
import static org.quackery.contract.collection.Factories.asFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Bugs {
  private final boolean isList, isImmutable, isForbiddingNull;
  private final String factory;

  private Bugs(boolean isList, String factory, boolean isImmutable, boolean isForbiddingNull) {
    this.isList = isList;
    this.factory = factory;
    this.isImmutable = isImmutable;
    this.isForbiddingNull = isForbiddingNull;
  }

  public Bugs() {
    this(false, null, false, false);
  }

  public Bugs list() {
    return new Bugs(true, factory, isImmutable, isForbiddingNull);
  }

  public Bugs factory(String factoryName) {
    return new Bugs(isList, factoryName, isImmutable, isForbiddingNull);
  }

  public Bugs immutable() {
    return new Bugs(isList, factory, true, isForbiddingNull);
  }

  public Bugs forbiddingNull() {
    return new Bugs(isList, factory, isImmutable, true);
  }

  public List<Class<?>> get() {
    List<Class<?>> bugs = new LinkedList<>();
    List<Class<?>> commonBugs = new LinkedList<>();

    addAllIf(
        true,
        bugs,
        org.quackery.contract.collection.bug.alien.Bugs.bugs);
    addAllIf(
        factory == null,
        bugs,
        org.quackery.contract.collection.bug.collection.constructor.Bugs.bugs);
    addAllIf(
        factory != null,
        bugs,
        org.quackery.contract.collection.bug.collection.factory.Bugs.bugs);
    addAllIf(
        isList && factory != null,
        bugs,
        org.quackery.contract.collection.bug.list.factory.Bugs.bugs);

    addAllIf(
        true,
        commonBugs,
        org.quackery.contract.collection.bug.collection.Bugs.bugs);
    addAllIf(
        isImmutable,
        commonBugs,
        org.quackery.contract.collection.bug.collection.immutable.Bugs.bugs);
    addAllIf(
        !isImmutable,
        commonBugs,
        org.quackery.contract.collection.bug.collection.mutable.Bugs.bugs);
    addAllIf(
        isForbiddingNull,
        commonBugs,
        org.quackery.contract.collection.bug.collection.forbiddingNull.Bugs.bugs);
    addAllIf(
        !isImmutable && isForbiddingNull,
        commonBugs,
        org.quackery.contract.collection.bug.collection.mutableForbiddingNull.Bugs.bugs);
    addAllIf(
        !isForbiddingNull,
        commonBugs,
        org.quackery.contract.collection.bug.collection.allowingNull.Bugs.bugs);
    addAllIf(
        !isImmutable && !isForbiddingNull,
        commonBugs,
        org.quackery.contract.collection.bug.collection.mutableAllowingNull.Bugs.bugs);
    addAllIf(
        isList,
        commonBugs,
        org.quackery.contract.collection.bug.list.Bugs.bugs);
    addAllIf(
        isList && isImmutable,
        commonBugs,
        org.quackery.contract.collection.bug.list.immutable.Bugs.bugs);
    addAllIf(
        isList && !isImmutable,
        commonBugs,
        org.quackery.contract.collection.bug.list.mutable.Bugs.bugs);

    bugs.addAll(factory != null
        ? asFactories(factory, commonBugs)
        : commonBugs);

    return unmodifiableList(bugs);
  }

  private static <T> void addAllIf(
      boolean condition,
      Collection<T> collection,
      Collection<? extends T> elements) {
    if (condition) {
      collection.addAll(elements);
    }
  }

  private static List<Class<?>> asFactories(String factory, List<Class<?>> bugs) {
    List<Class<?>> factoryBugs = new LinkedList<>();
    for (Class<?> bug : bugs) {
      factoryBugs.add(bug.isInstance(List.class)
          ? asFactory(List.class, factory, bug)
          : asFactory(Collection.class, factory, bug));
    }
    return factoryBugs;
  }
}
