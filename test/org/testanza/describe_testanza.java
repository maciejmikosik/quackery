package org.testanza;

public class describe_testanza {
  public static void main(String[] args) {
    describe_AnnotatedTestCase.is_runnable_with_one_annotated_method();
    describe_AnnotatedTestCase.fails_for_more_than_one_test_method();
    describe_AnnotatedTestCase.fails_for_no_test_methods();
    System.out.println("successful");
  }

  public static void verify(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }
}
