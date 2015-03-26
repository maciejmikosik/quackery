package org.quackery.testing.bug;

import static java.util.Collections.unmodifiableList;
import static org.quackery.testing.Tests.run;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.quackery.AssertionException;
import org.quackery.AssumptionException;
import org.quackery.Case;
import org.quackery.Contract;
import org.quackery.Suite;
import org.quackery.Test;

public class Expectations {
  public static void expectSuccess(Contract<Class<?>> contract, Class<?> implementation)
      throws Throwable {
    run(contract.test(implementation));
  }

  public static void expectFailure(Contract<Class<?>> contract, Class<?> implementation)
      throws Throwable {
    Test test = contract.test(implementation);
    Report report = runAndCatch(test);
    boolean expected = report.failures().size() > 0 && report.errors().size() == 0;
    if (!expected) {
      throw new AssertionError("expected failure of " + implementation.getName() + report);
    }
  }

  private static Report runAndCatch(Test test) {
    if (test instanceof Case) {
      Case cas = (Case) test;
      try {
        cas.run();
        return new Report().add(new Problem(cas, null));
      } catch (Throwable t) {
        return new Report().add(new Problem(cas, t));
      }
    } else if (test instanceof Suite) {
      Report report = new Report();
      for (Test subtest : ((Suite) test).tests) {
        report = report.merge(runAndCatch(subtest));
      }
      return report;
    } else {
      throw new RuntimeException("");
    }
  }

  private static class Report {
    public List<Problem> problems;

    private Report(List<Problem> problems) {
      this.problems = problems;
    }

    public Report() {
      this(unmodifiableList(new ArrayList<Problem>()));
    }

    public Report add(Problem problem) {
      List<Problem> newProblems = new ArrayList<>(problems);
      newProblems.add(problem);
      return new Report(unmodifiableList(newProblems));
    }

    public Report merge(Report report) {
      List<Problem> newProblems = new ArrayList<>(problems);
      newProblems.addAll(report.problems);
      return new Report(unmodifiableList(newProblems));
    }

    public List<Problem> failures() {
      List<Problem> failures = new ArrayList<>();
      for (Problem result : problems) {
        if (result.isFailure()) {
          failures.add(result);
        }
      }
      return failures;
    }

    public List<Problem> errors() {
      List<Problem> errors = new ArrayList<>();
      for (Problem result : problems) {
        if (result.isError()) {
          errors.add(result);
        }
      }
      return errors;
    }

    public List<Problem> assumptions() {
      List<Problem> assumptions = new ArrayList<>();
      for (Problem result : problems) {
        if (result.isError()) {
          assumptions.add(result);
        }
      }
      return assumptions;
    }

    public String toString() {
      List<Problem> failures = failures();
      List<Problem> errors = errors();
      List<Problem> assumptions = assumptions();

      StringBuilder builder = new StringBuilder();
      builder.append("\nfound ").append(failures.size()).append(" failures:");
      for (Problem result : failures) {
        builder.append("\n").append(result.test.name).append("\n");
      }
      builder.append("\nfound ").append(errors.size()).append(" errors:");
      for (Problem result : errors) {
        builder.append("\n").append(result.test.name).append("\n");
        builder.append(printStackTrace(result.throwable));
      }
      builder.append("\nfound ").append(assumptions.size()).append(" assumptions:");
      for (Problem result : assumptions) {
        builder.append("\n").append(result.test.name).append("\n");
      }
      return builder.toString();
    }

    private static String printStackTrace(Throwable throwable) {
      StringWriter writer = new StringWriter();
      throwable.printStackTrace(new PrintWriter(writer));
      return writer.toString();
    }
  }

  private static class Problem {
    public final Case test;
    public final Throwable throwable;

    public Problem(Case test, Throwable problem) {
      this.test = test;
      this.throwable = problem;
    }

    public boolean isSuccess() {
      return throwable == null;
    }

    public boolean isFailure() {
      return throwable instanceof AssertionException;
    }

    public boolean isAssumption() {
      return throwable instanceof AssumptionException;
    }

    public boolean isError() {
      return !isSuccess() && !isFailure() && !isAssumption();
    }
  }
}
