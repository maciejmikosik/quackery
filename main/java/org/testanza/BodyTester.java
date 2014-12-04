package org.testanza;

import static java.lang.System.identityHashCode;
import junit.framework.Test;
import junit.framework.TestCase;

public abstract class BodyTester<T> implements Tester<T> {
  public Test test(final T item) {
    return new TestCase(uniqueName(item)) {
      protected void runTest() throws Throwable {
        body(item);
      }
    };
  }

  private String uniqueName(final T item) {
    return name(item) + " #" + identityHashCode(item);
  }

  protected abstract String name(T item);

  protected abstract void body(T item) throws Throwable;
}
