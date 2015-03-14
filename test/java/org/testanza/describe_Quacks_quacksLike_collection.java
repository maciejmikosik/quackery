package org.testanza;

import static java.util.Arrays.asList;
import static org.testanza.Quacks.quacksLike;
import static org.testanza.testing.Tests.run;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.testanza.testing.bug.collect.MutableList;

public class describe_Quacks_quacksLike_collection {
  private Tester<Class<?>> tester;

  public void accepts_mutable_list() throws Throwable {
    run(quacksLike(Collection.class).test(MutableList.class));
  }

  public void accepts_jdk_collections() throws Throwable {
    tester = quacksLike(Collection.class);
    run(tester.test(ArrayList.class));
    run(tester.test(LinkedList.class));
    run(tester.test(HashSet.class));
    run(tester.test(TreeSet.class));
  }

  public void detects_collection_bugs_in_mutable_list() throws Throwable {
    tester = quacksLike(Collection.class);
    expectDetection(tester.test(MutableList.DefaultConstructorIsMissing.class));
    expectDetection(tester.test(MutableList.DefaultConstructorIsHidden.class));
    expectDetection(tester.test(MutableList.DefaultConstructorAddsElement.class));
    expectDetection(tester.test(MutableList.CopyConstructorIsMissing.class));
    expectDetection(tester.test(MutableList.CopyConstructorIsHidden.class));
    expectDetection(tester.test(MutableList.CopyConstructorCreatesEmpty.class));
    expectDetection(tester.test(MutableList.CopyConstructorAddsElement.class));
    expectDetection(tester.test(MutableList.CopyConstructorAcceptsNull.class));
    expectDetection(tester.test(MutableList.CopyConstructorThrowsRuntimeExceptionUponNull.class));
    expectDetection(tester.test(MutableList.CopyConstructorMakesNoDefensiveCopy.class));
    expectDetection(tester.test(MutableList.CopyConstructorModifiesArgument.class));
    expectDetection(tester.test(MutableList.CopyConstructorCreatesFixed.class));
  }

  private static void expectDetection(Test test) throws Throwable {
    List<Result> failures = new ArrayList<>();
    List<Result> errors = new ArrayList<>();
    for (Result result : runAndCatch(test)) {
      if (result.problem instanceof TestanzaAssertionException) {
        failures.add(result);
      } else if (result.problem == null) {

      } else if (result.problem instanceof TestanzaAssumptionException) {

      } else {
        errors.add(result);
      }
    }
    if (failures.size() != 1) {
      StringBuilder builder = new StringBuilder();
      builder.append("found ").append(failures.size()).append(" failures:");
      for (Result result : failures) {
        builder.append("\n").append(result.test.name);
      }
      throw new AssertionError(builder.toString());
    }
    if (errors.size() > 0) {
      StringBuilder builder = new StringBuilder();
      builder.append("found ").append(errors.size()).append(" errors:");
      for (Result result : errors) {
        builder.append("\n").append(result.test.name);
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
