package org.quackery.testing;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static java.util.Objects.hash;
import static org.quackery.Case.newCase;
import static org.quackery.testing.Type.CASE;
import static org.quackery.testing.Type.SUITE;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.quackery.Body;
import org.quackery.Contract;
import org.quackery.Test;

public class Testing {
  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError();
    }
  }

  public static void assertEquals(Object actual, Object expected) {
    if (!deepEquals(actual, expected)) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected equal to\n"
          + "    %s\n"
          + "  but was\n"
          + "    %s\n",
          expected,
          actual));
    }
  }

  public static void assertNotEquals(Object actual, Object expected) {
    if (deepEquals(actual, expected)) {
      throw new AssertionError(format(""
          + "\n"
          + "  expected not equal to\n"
          + "    %s\n"
          + "  but was\n"
          + "    %s\n",
          expected,
          actual));
    }
  }

  public static void assertChildren(Test actualTest, List<Test> expectedChildren) {
    actualTest.visit(
        (name, body) -> {
          throw new AssertionError();
        },
        (name, children) -> {
          assertEquals(children, expectedChildren);
          return null;
        });
  }

  public static Type typeOf(Test test) {
    return test.visit(
        (name, body) -> CASE,
        (name, children) -> SUITE);
  }

  public static String nameOf(Test test) {
    return test.visit(
        (name, body) -> name,
        (name, children) -> name);
  }

  public static List<Test> childrenOf(Test test) {
    return test.visit(
        (name, body) -> {
          throw new AssertionError();
        },
        (name, children) -> children);
  }

  public static void runAndThrow(Test test) throws Throwable {
    Optional<Throwable> thrown = runAndCatch(test);
    if (thrown.isPresent()) {
      throw thrown.get();
    }
  }

  public static Optional<Throwable> runAndCatch(Test test) {
    return test.visit(
        (name, body) -> {
          try {
            body.run();
          } catch (Throwable throwable) {
            return Optional.of(throwable);
          }
          return Optional.empty();
        },
        (name, children) -> {
          throw new IllegalArgumentException();
        });
  }

  public static void fail() {
    throw new AssertionError();
  }

  public static Object mockObject(final String name) {
    return new Object() {
      public boolean equals(Object object) {
        return object != null
            && getClass() == object.getClass()
            && toString().equals(object.toString());
      }

      public int hashCode() {
        return name.hashCode();
      }

      public String toString() {
        return name;
      }
    };
  }

  public static Test mockCase(String name) {
    return newCase(name, () -> {});
  }

  public static Test mockCase(String name, Throwable throwable) {
    return newCase(name, () -> {
      throw throwable.fillInStackTrace();
    });
  }

  public static <T> Contract<T> mockContract(final String name) {
    return new Contract<T>() {
      public Test test(T item) {
        return new MockTest(item, this);
      }

      public String toString() {
        return format("mockContract(%s)", name);
      }
    };
  }

  private static class MockTest extends Test {
    public final Object item;
    public final Contract<?> contract;

    public MockTest(Object item, Contract<?> contract) {
      super(format("%s.test(%s)", contract, item));
      this.item = item;
      this.contract = contract;
    }

    public <R> R visit(
        BiFunction<String, Body, R> caseHandler,
        BiFunction<String, List<Test>, R> suiteHandler) {
      throw new UnsupportedOperationException();
    }

    public boolean equals(Object object) {
      return object instanceof MockTest
          && equals((MockTest) object);
    }

    public boolean equals(MockTest that) {
      return item.equals(that.item)
          && contract.equals(that.contract);
    }

    public int hashCode() {
      return hash(item, contract);
    }

    public String toString() {
      return name;
    }
  }

  public static void interruptMeAfter(final double time) {
    final Thread caller = Thread.currentThread();
    Thread interrupter = new Thread(new Runnable() {
      public void run() {
        try {
          sleep(time);
        } catch (InterruptedException e) {
          throw new Error(e);
        }
        caller.interrupt();
      }
    });
    interrupter.start();
  }

  public static void sleep(double time) throws InterruptedException {
    Thread.sleep((long) (time * 1e3));
  }

  public static void sleepBusy(double time) {
    long nanos = (long) (time * 1e9);
    long start = System.nanoTime();
    while (System.nanoTime() - start < nanos) {}
  }
}
