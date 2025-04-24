package org.quackery;

import static org.quackery.QuackeryException.check;
import static org.quackery.Story.story;
import static org.quackery.Suite.suite;

import java.util.function.Function;
import java.util.function.Predicate;

public class Tests {
  public static <T extends Test> Function<Test, T> deep(Function<Test, T> change) {
    return test -> switch (test) {
      case Story story -> change.apply(story);
      case Suite suite -> change.apply(suite(suite.name)
          .addAll(suite.children.stream()
              .map(deep(change))
              .toList()));
    };
  }

  public static Function<Suite, Suite> retain(Predicate<? super Test> predicate) {
    check(predicate != null);
    return suite -> suite(suite.name).addAll(suite.children.stream()
        .filter(predicate)
        .toList());
  }

  public static Function<Suite, Suite> remove(Predicate<? super Test> predicate) {
    check(predicate != null);
    return retain(predicate.negate());
  }

  public static Function<Test, Test> ifStory(Function<Story, ? extends Test> change) {
    return test -> switch (test) {
      case Story story -> change.apply(story);
      default -> test;
    };
  }

  public static Function<Test, Test> ifSuite(Function<Suite, ? extends Test> change) {
    return test -> switch (test) {
      case Suite suite -> change.apply(suite);
      default -> test;
    };
  }

  public static Function<Test, Test> onName(Function<String, String> change) {
    return test -> switch (test) {
      case Story story -> story(change.apply(story.name), story.script);
      case Suite suite -> suite(change.apply(suite.name)).addAll(suite.children);
    };
  }

  public static Function<Story, Story> onScript(Function<Script, Script> change) {
    return story -> story(story.name, change.apply(story.script));
  }
}
