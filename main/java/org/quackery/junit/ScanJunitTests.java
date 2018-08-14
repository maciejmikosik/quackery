package org.quackery.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.quackery.Case;
import org.quackery.Test;

public class ScanJunitTests {
  public static Runner scanJunitTests(Class<?> annotatedClass) throws InitializationError {
    return new BlockJUnit4ClassRunner(annotatedClass);
  }

  public static List<Test> causes(InitializationError error) {
    boolean hasJunitTestMethods = hasJunitTestMethods(error);

    List<Test> errors = new ArrayList<>();
    for (final Throwable cause : error.getCauses()) {
      if (noRunnableMethods(cause)) {
        continue;
      } else if (noPublicDefaultConstructor(cause) && !hasJunitTestMethods) {
        continue;
      } else {
        errors.add(new Case(cause.getMessage()) {
          public void run() throws Throwable {
            throw cause;
          }
        });
      }
    }
    return errors;
  }

  private static boolean hasJunitTestMethods(InitializationError error) {
    for (Throwable cause : error.getCauses()) {
      if (noRunnableMethods(cause)) {
        return false;
      }
    }
    return true;
  }

  private static boolean noRunnableMethods(Throwable cause) {
    return cause.getMessage().equals("No runnable methods");
  }

  private static boolean noPublicDefaultConstructor(Throwable cause) {
    String message = cause.getMessage();
    return message.equals("Test class should have exactly one public constructor")
        || message.equals("Test class should have exactly one public zero-argument constructor");
  }
}
