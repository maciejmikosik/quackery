package org.testanza;

import static org.testanza.Case.newCase;

public abstract class BodyTester<T> implements Tester<T> {
  public Test test(final T item) {
    return newCase(name(item), new Closure() {
      public void invoke() throws Throwable {
        body(item);
      }
    });
  }

  protected abstract String name(T item);

  protected abstract void body(T item) throws Throwable;
}
