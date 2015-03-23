package org.quackery;

import static org.quackery.Quacks.quacksLike;
import static org.quackery.testing.Tests.run;
import static org.quackery.testing.bug.Bugs.bugs;
import static org.quackery.testing.bug.Expectations.expectFailure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.quackery.testing.bug.collect.MutableList;

public class describe_Quacks_quacksLike_collection {
  private Tester<Class<?>> tester;
  private List<Class<?>> bugs;

  public void accepts_mutable_list() throws Throwable {
    run(quacksLike(Collection.class).test(MutableList.class));
  }

  public void accepts_jdk_collections() throws Throwable {
    tester = quacksLike(Collection.class);
    run(tester.test(ArrayList.class));
    run(tester.test(LinkedList.class));
    run(tester.test(HashSet.class));
    run(tester.test(TreeSet.class));
  }

  public void detects_wrong_type() throws Throwable {
    tester = quacksLike(Collection.class);
    expectFailure(tester, Object.class);
  }

  public void detects_collection_bugs_in_mutable_list() throws Throwable {
    tester = quacksLike(Collection.class);
    bugs = bugs(Collection.class);
    for (Class<?> bug : bugs) {
      expectFailure(tester, bug);
    }
  }
}
