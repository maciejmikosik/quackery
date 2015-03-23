package org.quackery;

import static java.util.Arrays.asList;

class Build {
  Object core = asList(Test.class, Case.class, Suite.class, Contract.class);
  Object contracts = asList(Testers.class, Contracts.class);
  Object integration = asList(Quackery.class, QuackeryRunner.class);
}
