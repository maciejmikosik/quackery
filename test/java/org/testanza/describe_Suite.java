package org.testanza;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.testanza.Suite.newSuite;
import static org.testanza.Testilities.verify;
import static org.testanza.Testilities.verifyEquals;
import static org.testanza.Testilities.verifyFail;

import java.util.Arrays;
import java.util.List;

public class describe_Suite {
  private String name = "name";
  private final Test testA = new Test() {}, testB = new Test() {}, testC = new Test() {};
  private List<Test> tests = unmodifiableList(asList(testA, testB, testC));
  private Suite test;

  public void implements_test_interface() {
    verify(Test.class.isAssignableFrom(Suite.class));
  }

  public void assigns_fields() {
    test = newSuite(name, tests);
    verifyEquals(test.name, name);
    verifyEquals(test.tests, tests);
  }

  public void tests_list_is_covariant() {
    newSuite(name, Arrays.<Case> asList());
  }

  public void name_cannot_be_null() {
    name = null;
    try {
      newSuite(name, tests);
      verifyFail();
    } catch (TestanzaException e) {}
  }

  public void tests_cannot_be_null() {
    tests = null;
    try {
      newSuite(name, tests);
      verifyFail();
    } catch (TestanzaException e) {}
  }

  public void tests_cannot_contain_null() {
    tests = asList(testA, null, testC);
    try {
      newSuite(name, tests);
      verifyFail();
    } catch (TestanzaException e) {}
  }
}
