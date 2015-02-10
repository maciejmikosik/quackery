package org.testanza;

import static org.testanza.TestanzaException.check;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

class Helpers {
  public static <T> Tester<T> asTester(final Matcher<T> matcher) {
    check(matcher != null);
    return new Tester<T>() {
      public Test test(final T item) {
        return new Case(item + " is " + matcher.toString()) {
          public void run() {
            if (!matcher.matches(item)) {
              throw new TestanzaAssertionError("" //
                  + "\n" //
                  + "  expected that\n" //
                  + "    " + item + "\n" //
                  + "  matches\n" //
                  + "    " + matcher + "\n" //
                  + "  but\n" //
                  + "    " + diagnose(item, matcher) + "\n" //
              );
            }
          }
        };
      }
    };
  }

  private static <T> String diagnose(T item, Matcher<T> matcher) {
    StringDescription description = new StringDescription();
    matcher.describeMismatch(item, description);
    return description.toString();
  }
}
