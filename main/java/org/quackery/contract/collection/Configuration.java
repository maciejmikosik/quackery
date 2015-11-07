package org.quackery.contract.collection;

import static java.util.Collections.unmodifiableSet;
import static org.quackery.QuackeryException.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration {
  private final boolean immutable, forbiddingNull;
  private final Set<Class<?>> implementing;
  private final String factoryName;

  public Configuration() {
    immutable = false;
    forbiddingNull = false;
    implementing = unmodifiableSet(new HashSet<Class<?>>());
    factoryName = null;
  }

  private Configuration(boolean immutable, boolean forbiddingNull,
      Set<Class<?>> implementing, String factoryName) {
    this.immutable = immutable;
    this.forbiddingNull = forbiddingNull;
    this.implementing = implementing;
    this.factoryName = factoryName;
  }

  public Configuration implementing(Class<?> type) {
    check(canBeImplemented(type));
    check(!implementing.contains(type));
    Set<Class<?>> newImplementing = new HashSet<>(implementing);
    newImplementing.add(type);
    return new Configuration(immutable, forbiddingNull,
        unmodifiableSet(newImplementing), factoryName);
  }

  private static boolean canBeImplemented(Class<?> type) {
    return type == List.class;
  }

  public Configuration immutable() {
    check(immutable == false);
    return new Configuration(true, forbiddingNull, implementing, factoryName);
  }

  public Configuration forbidding(Void object) {
    check(forbiddingNull == false);
    return new Configuration(immutable, true, implementing, factoryName);
  }

  public Configuration withFactory(String methodName) {
    check(methodName != null);
    check(factoryName == null);
    return new Configuration(immutable, forbiddingNull, implementing, methodName);
  }

  public boolean isImplementing(Class<?> type) {
    return implementing.contains(type);
  }

  public boolean hasConstructor() {
    return !hasFactory();
  }

  public boolean hasFactory() {
    return factoryName != null;
  }

  public String getFactoryName() {
    return factoryName;
  }

  public boolean isImmutable() {
    return immutable;
  }

  public boolean isForbiddingNull() {
    return forbiddingNull;
  }

  public Class<?> getCollectionType() {
    return implementing.contains(List.class)
        ? List.class
        : Collection.class;
  }
}
