package org.quackery;

import static java.lang.String.format;
import static net.bytebuddy.TestByteBuddy.test_byte_buddy;
import static org.quackery.TestCase.test_case;
import static org.quackery.TestSuite.test_suite;
import static org.quackery.contract.collection.TestCollectionContract.test_collection_contract;
import static org.quackery.junit.TestQuackeryRunner.test_quackery_runner;
import static org.quackery.report.TestReportsCountThrowables.test_reports_count_throwables;
import static org.quackery.report.TestReportsFormat.test_reports_format;
import static org.quackery.run.TestRunnersClassLoaderScoped.test_runners_class_loader_scoped;
import static org.quackery.run.TestRunnersRun.test_runners_run;
import static org.quackery.run.TestRunnersRunConcurrent.test_runners_run_concurrent;
import static org.quackery.run.TestRunnersRunIn.test_runners_run_in;
import static org.quackery.run.TestRunnersThreadScoped.test_runners_thread_scoped;
import static org.quackery.run.TestRunnersTimeout.test_runners_timeout;

public class TestAll {
  public static void main(String[] args) throws Throwable {
    long start = System.nanoTime();

    test_byte_buddy();

    test_case();
    test_suite();

    test_runners_run();
    test_runners_run_in();
    test_runners_run_concurrent();
    test_runners_timeout();
    test_runners_thread_scoped();
    test_runners_class_loader_scoped();

    test_reports_count_throwables();
    test_reports_format();

    test_quackery_runner();

    test_collection_contract();

    long stop = System.nanoTime();
    System.out.println(format("finished in %.3f seconds", 1E-9 * (stop - start)));
  }
}
