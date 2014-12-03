package org.testanza;

import junit.framework.Test;

public interface Tester<T> {
  Test test(T item);
}
