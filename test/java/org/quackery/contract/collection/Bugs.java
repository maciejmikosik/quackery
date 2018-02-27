package org.quackery.contract.collection;

import static java.util.Collections.unmodifiableList;
import static org.quackery.contract.collection.Factories.asFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.quackery.contract.collection.bug.alien.AlienBugs;
import org.quackery.contract.collection.bug.collection.CollectionBugs;
import org.quackery.contract.collection.bug.collection.allowingNull.CollectionAllowingNullBugs;
import org.quackery.contract.collection.bug.collection.constructor.CollectionConstructorBugsBugs;
import org.quackery.contract.collection.bug.collection.factory.CollectionFactoryBugs;
import org.quackery.contract.collection.bug.collection.forbiddingNull.CollectionForbiddingNullBugs;
import org.quackery.contract.collection.bug.collection.immutable.CollectionImmutableBugs;
import org.quackery.contract.collection.bug.collection.mutable.CollectionMutableBugs;
import org.quackery.contract.collection.bug.collection.mutableAllowingNull.CollectionMutableAllowingNullBugs;
import org.quackery.contract.collection.bug.collection.mutableForbiddingNull.CollectionMutableForbiddingNullBugs;
import org.quackery.contract.collection.bug.list.ListBugs;
import org.quackery.contract.collection.bug.list.factory.ListFactoryBugs;
import org.quackery.contract.collection.bug.list.immutable.ListImmutableBugs;
import org.quackery.contract.collection.bug.list.mutable.ListMutableBugs;

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

    bugs.addAll(onlyIf(true, AlienBugs.BUGS));
    bugs.addAll(onlyIf(factory == null, CollectionConstructorBugsBugs.BUGS));
    bugs.addAll(onlyIf(factory != null, CollectionFactoryBugs.BUGS));
    bugs.addAll(onlyIf(isList && factory != null, ListFactoryBugs.BUGS));

    commonBugs.addAll(onlyIf(true, CollectionBugs.BUGS));
    commonBugs.addAll(onlyIf(isImmutable, CollectionImmutableBugs.BUGS));
    commonBugs.addAll(onlyIf(!isImmutable, CollectionMutableBugs.BUGS));
    commonBugs.addAll(onlyIf(isForbiddingNull, CollectionForbiddingNullBugs.BUGS));
    commonBugs.addAll(onlyIf(!isImmutable && isForbiddingNull, CollectionMutableForbiddingNullBugs.BUGS));
    commonBugs.addAll(onlyIf(!isForbiddingNull, CollectionAllowingNullBugs.BUGS));
    commonBugs.addAll(onlyIf(!isImmutable && !isForbiddingNull, CollectionMutableAllowingNullBugs.BUGS));
    commonBugs.addAll(onlyIf(isList, ListBugs.BUGS));
    commonBugs.addAll(onlyIf(isList && isImmutable, ListImmutableBugs.BUGS));
    commonBugs.addAll(onlyIf(isList && !isImmutable, ListMutableBugs.BUGS));

    bugs.addAll(factory != null
        ? asFactories(factory, commonBugs)
        : commonBugs);

    return unmodifiableList(bugs);
  }

  private static <T> Collection<? extends T> onlyIf(boolean condition, Collection<? extends T> elements) {
    return condition ? elements : Arrays.<T> asList();
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
