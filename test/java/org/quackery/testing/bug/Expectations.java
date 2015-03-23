package org.quackery.testing.bug;

import static java.util.Arrays.asList;
import static org.quackery.testing.Tests.run;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.QuackeryAssertionException;
import org.quackery.QuackeryAssumptionException;
import org.quackery.Suite;
import org.quackery.Test;
import org.quackery.Tester;

public class Expectations {
  public static void expectSuccess(Tester<Class<?>> tester, Class<?> implementation)
      throws Throwable {
    run(tester.test(implementation));
  }

  public static void expectFailure(Tester<Class<?>> tester, Class<?> implementation)
      throws Throwable {
    Test test = tester.test(implementation);
    List<Result> failures = new ArrayList<>();
    List<Result> errors = new ArrayList<>();
    for (Result result : runAndCatch(test)) {
      if (result.problem instanceof QuackeryAssertionException) {
        failures.add(result);
      } else if (result.problem == null) {

      } else if (result.problem instanceof QuackeryAssumptionException) {

      } else {
        errors.add(result);
      }
    }
    if (failures.size() == 0) {
      StringBuilder builder = new StringBuilder();
      builder.append("found ").append(failures.size()).append(" failures:");
      for (Result result : failures) {
        builder.append("\n").append(result.test.name).append("\n");
      }
      throw new AssertionError(builder.toString());
    }
    if (errors.size() > 0) {
      StringBuilder builder = new StringBuilder();
      builder.append("found ").append(errors.size()).append(" errors:");
      for (Result result : errors) {
        builder.append("\n").append(result.test.name).append("\n");
        builder.append(printStackTrace(result.problem));
      }
      throw new AssertionError(builder.toString());
    }
  }

  private static String printStackTrace(Throwable throwable) {
    StringWriter writer = new StringWriter();
    throwable.printStackTrace(new PrintWriter(writer));
    return writer.toString();
  }

  private static List<Result> runAndCatch(Test test) {
    if (test instanceof Case) {
      Case cas = (Case) test;
      try {
        cas.run();
        return asList(new Result(cas, null));
      } catch (Throwable t) {
        return asList(new Result(cas, t));
      }
    } else if (test instanceof Suite) {
      List<Result> results = new ArrayList<>();
      for (Test subtest : ((Suite) test).tests) {
        results.addAll(runAndCatch(subtest));
      }
      return results;
    } else {
      throw new RuntimeException("");
    }
  }

  private static class Result {
    public final Case test;
    public final Throwable problem;

    public Result(Case test, Throwable problem) {
      this.test = test;
      this.problem = problem;
    }
  }
}
