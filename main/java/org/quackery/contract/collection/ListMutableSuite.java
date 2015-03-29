package org.quackery.contract.collection;

import static org.quackery.Suite.suite;

import org.quackery.Test;

public class ListMutableSuite {
  public static Test listMutableSuite(Class<?> type) {
    return suite("quacks like mutable list");
  }
}
