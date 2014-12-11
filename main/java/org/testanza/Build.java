package org.testanza;

import static java.util.Arrays.asList;

class Build {
  Object core = asList(Test.class, Case.class, Suite.class, Tester.class);
  Object helpers = asList(CaseTester.class, SuiteTester.class);
  Object testers = asList(Testers.class);
  Object integration = asList(Junit.class);
}
