package org.quackery.contract.collection.suite;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.assertThat;
import static org.quackery.Suite.suite;
import static org.quackery.contract.collection.Assumptions.assumeCreateList;
import static org.quackery.contract.collection.Collections.copy;
import static org.quackery.contract.collection.Collections.newArrayList;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Test;

public class ListMutableSuite {
  public static Test listMutableSuite(Class<?> type) {
    return suite("quacks like mutable list")
        .test(suite("overrides add")
            .test(addAddsElementAtTheEnd(type))
            .test(addReturnsTrue(type)));
  }

  private static Test addAddsElementAtTheEnd(final Class<?> type) {
    return new Case("add adds element at the end") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList("a", "b", "c");
        List<Object> list = assumeCreateList(type, copy(original));
        original.add("d");
        list.add("d");
        assertEquals(copy(list.toArray()), original.toArray());
      }
    };
  }

  private static Test addReturnsTrue(final Class<?> type) {
    return new Case("add returns true") {
      public void run() throws Throwable {
        List<Object> list = assumeCreateList(type, newArrayList("a", "b", "c"));
        assertThat(list.add("d"));
      }
    };
  }
}
