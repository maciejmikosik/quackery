package org.testanza;

import junit.framework.Test;
import junit.framework.TestCase;

public abstract class BodyTester<T> implements Tester<T> {
  public Test test(final T item) {
    return new TestCase(name(item)) {
      protected void runTest() throws Throwable {
        body(item);
      }
    };
  }

  protected abstract String name(T item);

  protected abstract void body(T item) throws Throwable;
}
