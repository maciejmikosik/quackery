package org.testanza;

public class describe_testanza {
  public static void main(String[] args) throws Throwable {
    describe_TestMembers_has_modifier.succeeds_if_method_has_modifier();
    describe_TestMembers_has_modifier.fails_if_method_has_no_modifier();
    describe_TestMembers_has_modifier.failure_prints_message();
    describe_TestMembers_has_modifier.test_name_contains_modifier();
    describe_TestMembers_has_modifier.test_name_contains_member_type_and_siple_name();
    describe_TestMembers_has_modifier.test_name_differs_even_if_members_have_same_simple_name();
    describe_TestMembers_has_modifier.test_name_is_same_for_equal_member();

    System.out.println("successful");
  }

  public static void verify(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }
}
