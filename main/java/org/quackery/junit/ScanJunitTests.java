package org.quackery.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.quackery.QuackeryException;
import org.quackery.Test;

public class ScanJunitTests {
  public static Runner scanJunitTests(Class<?> annotatedClass) throws InitializationError {
    return new BlockJUnit4ClassRunner(annotatedClass);
  }

  public static List<Test> causes(InitializationError error) {
    if (isBecauseNoRunnableMethods(error)) {
      return new ArrayList<>();
    } else {
      throw new QuackeryException(error);
    }
  }

  private static boolean isBecauseNoRunnableMethods(InitializationError error) {
    List<Throwable> causes = error.getCauses();
    return causes.size() == 1 && causes.get(0).getMessage().equals("No runnable methods");
  }
}
