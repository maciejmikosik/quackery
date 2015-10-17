package org.quackery.contract.collection;

import static java.util.Collections.unmodifiableSet;
import static org.quackery.QuackeryException.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration {
  private final boolean mutable, immutable;
  private final Set<Class<?>> implementing;
  private final String factoryName;

  public Configuration() {
    mutable = false;
    immutable = false;
    implementing = unmodifiableSet(new HashSet<Class<?>>());
    factoryName = null;
  }

  private Configuration(boolean mutable, boolean immutable, Set<Class<?>> implementing,
      String factoryName) {
    this.mutable = mutable;
    this.immutable = immutable;
    this.implementing = implementing;
    this.factoryName = factoryName;
  }

  public Configuration implementing(Class<?> type) {
    check(canBeImplemented(type));
    check(!implementing.contains(type));
    Set<Class<?>> newImplementing = new HashSet<>(implementing);
    newImplementing.add(type);
    return new Configuration(mutable, immutable, unmodifiableSet(newImplementing), factoryName);
  }

  private static boolean canBeImplemented(Class<?> type) {
    return type == List.class;
  }

  public Configuration mutable() {
    check(mutable == false);
    check(immutable == false);
    return new Configuration(true, immutable, implementing, factoryName);
  }

  public Configuration immutable() {
    check(immutable == false);
    check(mutable == false);
    return new Configuration(mutable, true, implementing, factoryName);
  }

  public Configuration withFactory(String methodName) {
    check(methodName != null);
    check(factoryName == null);
    return new Configuration(mutable, immutable, implementing, methodName);
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

  public boolean isImmutable() {
    return immutable;
  }

  public Class<?> getCollectionType() {
    return implementing.contains(List.class)
        ? List.class
        : Collection.class;
  }
}
