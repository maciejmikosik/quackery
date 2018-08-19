package org.quackery.help;

import static org.quackery.Suite.suite;

import java.util.ArrayList;
import java.util.List;

import org.quackery.Case;
import org.quackery.Suite;
import org.quackery.Test;

public abstract class TraversingDecorator implements Decorator {
  public final Test decorate(Test test) {
    if (test instanceof Suite) {
      List<Test> decoratedChildren = new ArrayList<>();
      for (Test child : ((Suite) test).tests) {
        decoratedChildren.add(decorate(child));
      }
      return decorateTest(decorateSuite(suite(test.name).addAll(decoratedChildren)));
    } else {
      return decorateTest(decorateCase((Case) test));
    }
  }

  protected Test decorateTest(Test test) {
    return test;
  }

  protected Test decorateCase(Case cas) {
    return cas;
  }

  protected Test decorateSuite(Suite suite) {
    return suite;
  }
}
