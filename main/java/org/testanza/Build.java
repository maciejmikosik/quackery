package org.testanza;

import static java.util.Arrays.asList;

import org.testanza.testers.TestersForClasses;

class Build {
  Object core = asList(Test.class, Case.class, Suite.class);
  Class<?> helpers = CaseTester.class;
  Class<?> testBuilder = TestBuilder.class;
  Class<?> testersForClasses = TestersForClasses.class;
}
