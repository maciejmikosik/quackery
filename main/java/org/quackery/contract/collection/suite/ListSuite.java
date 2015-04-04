package org.quackery.contract.collection.suite;

import static org.quackery.AssertionException.assertEquals;
import static org.quackery.AssertionException.fail;
import static org.quackery.contract.collection.Assumptions.assumeCreateList;
import static org.quackery.contract.collection.Collections.copy;
import static org.quackery.contract.collection.Collections.newArrayList;
import static org.quackery.contract.collection.Element.a;
import static org.quackery.contract.collection.Element.b;
import static org.quackery.contract.collection.Element.c;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Test;

public class ListSuite {
  public static Test instantiatorStoresAllElementsInOrder(final Class<?> type) {
    return new Case("stores all elements in order") {
      public void run() throws Throwable {
        run(newArrayList(a, b, c));
        run(newArrayList(a, c, b));
        run(newArrayList(b, a, c));
        run(newArrayList(b, c, a));
        run(newArrayList(c, a, b));
        run(newArrayList(c, b, a));
      }

      private void run(ArrayList<?> order) throws Throwable {
        List<?> list = assumeCreateList(type, copy(order));
        assertEquals(copy(list.toArray()), order.toArray());
      }
    };
  }

  public static Test getReturnsEachElement(final Class<?> type) {
    return new Case("returns each element") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<?> list = assumeCreateList(type, copy(original));
        for (int i = 0; i < original.size(); i++) {
          try {
            assertEquals(list.get(i), original.get(i));
          } catch (IndexOutOfBoundsException e) {
            fail();
          }
        }
      }
    };
  }

  public static Test getFailsForIndexAboveBound(final Class<?> type) {
    return new Case("fails for index above bound") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<?> list = assumeCreateList(type, copy(original));
        try {
          list.get(original.size());
          fail();
        } catch (IndexOutOfBoundsException e) {}
      }
    };
  }

  public static Test getFailsForIndexBelowBound(final Class<?> type) {
    return new Case("fails for index below bound") {
      public void run() throws Throwable {
        ArrayList<Object> original = newArrayList(a, b, c);
        List<?> list = assumeCreateList(type, copy(original));
        try {
          list.get(-1);
          fail();
        } catch (IndexOutOfBoundsException e) {}
      }
    };
  }
}
