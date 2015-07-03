package org.quackery.contract.collection;

import static org.quackery.QuackeryException.check;

import org.quackery.Contract;
import org.quackery.Test;

public class CollectionContract implements Contract<Class<?>> {
  private final Configuration configuration;

  public CollectionContract() {
    configuration = new Configuration();
  }

  private CollectionContract(Configuration configuration) {
    this.configuration = configuration;
  }

  public Test test(Class<?> type) {
    check(type != null);
    return CollectionTests.test(configuration, type);
  }

  public CollectionContract implementing(Class<?> type) {
    return new CollectionContract(configuration.implementing(type));
  }

  public CollectionContract mutable() {
    return new CollectionContract(configuration.mutable());
  }

  public CollectionContract withFactory(String factoryMethodName) {
    return new CollectionContract(configuration.withFactory(factoryMethodName));
  }
}
