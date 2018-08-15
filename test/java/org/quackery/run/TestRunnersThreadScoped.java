package org.quackery.run;

import static org.quackery.run.Runners.threadScoped;
import static org.quackery.testing.Testing.assertTrue;

import org.quackery.Case;
import org.quackery.Test;

public class TestRunnersThreadScoped extends TestVisitor {
  private final String name = "name";
  private Test test, testA, testB;
  private Thread callerThread, scopingThread, scopingThreadA, scopingThreadB;

  protected Test visit(Test visiting) {
    return threadScoped(visiting);
  }

  public void runs_test_in_different_thread_than_caller() throws Throwable {
    callerThread = Thread.currentThread();
    test = threadScoped(new Case(name) {
      public void run() {
        scopingThread = Thread.currentThread();
      }
    });

    // when
    ((Case) test).run();

    // then
    assertTrue(scopingThread != null);
    assertTrue(scopingThread != callerThread);
  }

  public void runs_each_test_in_different_thread() throws Throwable {
    testA = threadScoped(new Case(name) {
      public void run() {
        scopingThreadA = Thread.currentThread();
      }
    });
    testB = threadScoped(new Case(name) {
      public void run() {
        scopingThreadB = Thread.currentThread();
      }
    });

    // when
    ((Case) testA).run();
    ((Case) testB).run();

    // then
    assertTrue(scopingThreadA != null);
    assertTrue(scopingThreadB != null);
    assertTrue(scopingThreadA != scopingThreadB);
  }
}
