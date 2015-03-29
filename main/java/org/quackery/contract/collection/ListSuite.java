package org.quackery.contract.collection;

import static org.quackery.Suite.suite;

import org.quackery.Test;

public class ListSuite {
  public static Test listSuite(Class<?> type) {
    return suite("quacks like list");
  }
}
