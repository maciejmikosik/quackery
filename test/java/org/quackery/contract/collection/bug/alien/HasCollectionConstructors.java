package org.quackery.contract.collection.bug.alien;

import java.util.Collection;

public class HasCollectionConstructors {
  public HasCollectionConstructors() {}

  public HasCollectionConstructors(Collection<?> collection) {
    if (collection == null) {
      throw new NullPointerException();
    }
  }
}
