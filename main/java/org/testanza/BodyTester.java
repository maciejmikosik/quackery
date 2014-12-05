package org.testanza;

import junit.framework.Test;
import junit.framework.TestCase;

public abstract class BodyTester<T> implements Tester<T> {
  private static final Namer namer = new Namer();

  public Test test(final T item) {
    TestCase test = new TestCase(name(item)) {
      protected void runTest() throws Throwable {
        body(item);
      }
    };
    namer.makeNameUnique(test);
    return test;
  }

  protected abstract String name(T item);

  protected abstract void body(T item) throws Throwable;
}
