package org.quackery.junit;

import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.quackery.QuackeryException;

public class ScanJunitTests {
  public static Runner scanJunitTests(Class<?> annotatedClass) {
    try {
      return new BlockJUnit4ClassRunner(annotatedClass);
    } catch (InitializationError e) {
      if (isBecauseNoRunnableMethods(e)) {
        return null;
      } else {
        throw new QuackeryException(e);
      }
    }
  }

  private static boolean isBecauseNoRunnableMethods(InitializationError error) {
    List<Throwable> causes = error.getCauses();
    return causes.size() == 1 && causes.get(0).getMessage().equals("No runnable methods");
  }

}
