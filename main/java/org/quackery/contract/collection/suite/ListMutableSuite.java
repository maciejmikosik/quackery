package org.quackery.contract.collection.suite;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.assertThat;
import static org.quackery.contract.collection.Collections.copy;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;
import static org.quackery.contract.collection.Element.b;
import static org.quackery.contract.collection.Element.c;
import static org.quackery.contract.collection.Element.d;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Test;
import org.quackery.contract.collection.Creator;

public class ListMutableSuite {
  public static Test addAddsElementAtTheEnd(final Creator creator) {
    return new Case("adds element at the end") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<Object> list = creator.create(List.class, copy(original));
        original.add(d);
        list.add(d);
        assertEquals(copy(list.toArray()), original.toArray());
      }
    };
  }

  public static Test addReturnsTrue(final Creator creator) {
    return new Case("returns true") {
      public void run() throws Throwable {
        List<Object> list = creator.create(List.class, newArrayList(a, b, c));
        assertThat(list.add(d));
      }
    };
  }
}
