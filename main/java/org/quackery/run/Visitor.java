package org.quackery.run;

import static org.quackery.QuackeryException.check;
import static org.quackery.Suite.suite;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public class Visitor {
  public Test visit(Test visiting) {
    check(visiting != null);
    return visiting instanceof Case
        ? visit((Case) visiting)
        : visit((Suite) visiting);
  }

  protected Test visit(Case visiting) {
    return visiting;
  }

  protected Test visit(Suite visiting) {
    List<Test> visitedChildren = new ArrayList<>();
    for (Test child : visiting.tests) {
      visitedChildren.add(child instanceof Case
          ? visit((Case) child)
          : visit((Suite) child));
    }
    return suite(visiting.name)
        .addAll(visitedChildren);
  }
}
