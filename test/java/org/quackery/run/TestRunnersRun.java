package org.quackery.run;

import static org.quackery.run.Runners.run;
import static org.quackery.run.TestingDecorators.decorator_preserves_case_result;
import static org.quackery.run.TestingDecorators.decorator_preserves_names_and_structure;
import static org.quackery.run.TestingDecorators.decorator_runs_cases_eagerly;
import static org.quackery.run.TestingDecorators.decorator_validates_arguments;

import java.util.function.Function;

import org.quackery.Test;

public class TestRunnersRun {
  public static void test_runners_run() throws Throwable {
    Function<Test, Test> decorate = test -> run(test);

    decorator_preserves_names_and_structure(decorate);
    decorator_preserves_case_result(decorate);
    decorator_validates_arguments(decorate);
    decorator_runs_cases_eagerly(decorate);
  }
}
