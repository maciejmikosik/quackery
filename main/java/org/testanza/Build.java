package org.testanza;

import static java.util.Arrays.asList;

class Build {
  Object core = asList(Test.class, Case.class, Suite.class);
  Class<?> helpers = CaseTester.class;
  Class<?> testBuilder = TestBuilder.class;
  Object testers = asList(Testers.class);
}
