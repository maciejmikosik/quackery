package org.quackery.help;

import static org.quackery.Suite.suite;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Helpers {
  public static Case successfulCase(String name) {
    return new Case(name) {
      public void run() {}
    };
  }

  public static Case failingCase(String name, final Throwable throwable) {
    return new Case(name) {
      public void run() throws Throwable {
        throw throwable;
      }
    };
  }

  public static Test rename(String name, final Test test) {
    return test instanceof Case
        ? rename(name, (Case) test)
        : rename(name, (Suite) test);
  }

  public static Case rename(String name, final Case test) {
    return new Case(name) {
      public void run() throws Throwable {
        test.run();
      }
    };
  }

  public static Suite rename(String name, final Suite test) {
    return suite(name).addAll(test.tests);
  }
}
