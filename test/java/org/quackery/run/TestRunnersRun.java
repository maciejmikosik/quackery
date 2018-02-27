package org.quackery.run;

import static org.quackery.run.Runners.run;

import org.quackery.Test;

public class TestRunnersRun extends TestRunner {
  protected Test visit(Test visited) {
    return run(visited);
  }
}
