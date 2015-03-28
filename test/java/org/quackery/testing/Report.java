package org.quackery.testing;

import static java.util.Collections.unmodifiableList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Report {
  public final List<Problem> problems;

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

  public List<Problem> misassumptions() {
    List<Problem> misassumptions = new ArrayList<>();
    for (Problem result : problems) {
      if (result.isMisassumption()) {
        misassumptions.add(result);
      }
    }
    return misassumptions;
  }

  public String toString() {
    List<Problem> failures = failures();
    List<Problem> errors = errors();
    List<Problem> misassumptions = misassumptions();

    StringBuilder builder = new StringBuilder("\n");
    builder.append("found ").append(failures.size()).append(" failures\n");
    for (Problem result : failures) {
      builder.append(result.test.name).append("\n");
    }
    builder.append("found ").append(errors.size()).append(" errors\n");
    for (Problem result : errors) {
      builder.append(result.test.name).append("\n");
      builder.append(printStackTrace(result.throwable));
    }
    builder.append("found ").append(misassumptions.size()).append(" misassumptions\n");
    for (Problem result : misassumptions) {
      builder.append(result.test.name).append("\n");
    }
    return builder.toString();
  }

  private static String printStackTrace(Throwable throwable) {
    StringWriter writer = new StringWriter();
    throwable.printStackTrace(new PrintWriter(writer));
    return writer.toString();
  }
}
