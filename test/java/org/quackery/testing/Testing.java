package org.quackery.testing;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static java.util.Objects.hash;

import org.quackery.Body;
import org.quackery.Case;
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

  public static Body mockBody() {
    return new Body() {
      public void run() {}
    };
  }

  public static Case mockCase(String name) {
    return new Case(name) {
      public void run() {}
    };
  }

  public static Case mockCase(String name, final Throwable throwable) {
    return new Case(name) {
      public void run() throws Throwable {
        throw throwable.fillInStackTrace();
      }
    };
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
