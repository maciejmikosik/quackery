package org.quackery;

import static java.util.Arrays.asList;

class Build {
  Object core = asList(Test.class, Case.class, Suite.class, Tester.class);
  Object testers = asList(Testers.class, Quacks.class);
  Object integration = asList(Junit.class, Quackery.class, QuackeryRunner.class);
}
