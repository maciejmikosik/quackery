package org.testanza;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import junit.framework.TestCase;

public class Namer {
  private final Map<String, WeakReference<TestCase>> names = new WeakHashMap<String, WeakReference<TestCase>>();

  public synchronized void makeNameUnique(TestCase test) {
    String name = test.getName();
    if (!names.containsKey(name)) {
      names.put(name, new WeakReference<TestCase>(test));
    } else if (!test.equals(names.get(name).get())) {
      for (int i = 1;; i++) {
        String newName = name + " #" + i;
        if (!names.containsKey(newName)) {
          names.put(newName, new WeakReference<TestCase>(test));
          test.setName(newName);
          break;
        }
      }
    }
  }
}
