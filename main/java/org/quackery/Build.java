package org.quackery;

import static java.util.Arrays.asList;

import org.quackery.junit.QuackeryRunner;
import org.quackery.report.AssertException;
import org.quackery.report.AssumeException;
import org.quackery.report.ReportException;
import org.quackery.run.Reports;
import org.quackery.run.Runners;

class Build {
  Object core = asList(Test.class, Case.class, Suite.class, Contract.class, QuackeryException.class);
  Object report = asList(ReportException.class, AssertException.class, AssumeException.class);
  Object contracts = asList(Contracts.class);
  Object integration = asList(Quackery.class, QuackeryRunner.class);
  Object run = asList(Runners.class, Reports.class);
}
