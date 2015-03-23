package org.quackery;

public interface Contract<T> {
  Test test(T item);
}
