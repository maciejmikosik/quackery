package org.quackery.contract.collection;

import static java.util.Collections.unmodifiableSet;
import static org.quackery.QuackeryException.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration {
  private final boolean mutable;
  private final Set<Class<?>> implementing;
  private final String factoryName;

  public Configuration() {
    mutable = false;
    implementing = unmodifiableSet(new HashSet<Class<?>>());
    factoryName = null;
  }

  private Configuration(boolean mutable, Set<Class<?>> implementing, String factoryName) {
    this.mutable = mutable;
    this.implementing = implementing;
    this.factoryName = factoryName;
  }

  public Configuration implementing(Class<?> type) {
    check(canBeImplemented(type));
    check(!implementing.contains(type));
    Set<Class<?>> newImplementing = new HashSet<>(implementing);
    newImplementing.add(type);
    return new Configuration(mutable, unmodifiableSet(newImplementing), factoryName);
  }

  private static boolean canBeImplemented(Class<?> type) {
    return type == List.class;
  }

  public Configuration mutable() {
    check(mutable == false);
    return new Configuration(true, implementing, factoryName);
  }

  public Configuration withFactory(String methodName) {
    check(methodName != null);
    check(factoryName == null);
    return new Configuration(mutable, implementing, methodName);
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

  public boolean isMutable() {
    return mutable;
  }

  public Class<?> getCollectionType() {
    return implementing.contains(List.class)
        ? List.class
        : Collection.class;
  }
}
