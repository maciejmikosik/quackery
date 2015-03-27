package org.quackery.contract.collection;

import static org.quackery.Suite.newSuite;

import org.quackery.Test;

public class CollectionMutableSuite {
  public static Test collectionMutableSuite(Class<?> type) {
    return newSuite(type.getName() + " quacks like mutable collection");
  }
}
