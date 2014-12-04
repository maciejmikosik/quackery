package org.testanza;

import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;

public class describe_TestBuilder {
  private static TestBuilder builder;

  public static void accepts_covariant_list() {
    List<String> items = Arrays.asList();
    Tester<Object> tester = new Tester<Object>() {
      public Test test(Object item) {
        return new TestCase() {};
      }
    };
    builder = new TestBuilder("");
    // verify that compiles
    builder.testThatAll(items, tester);
  }
}