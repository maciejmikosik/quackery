package org.quackery.run;

import static org.quackery.run.Runners.run;

import org.quackery.Test;

public class test_Runners_run extends test_Runner {
  protected Test visit(Test visited) {
    return run(visited);
  }
}
