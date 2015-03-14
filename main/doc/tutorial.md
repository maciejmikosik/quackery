
### Quick glance

Quackery contains pre-written test suites for well-known contracts.
If you are implementing your own collection (let's say `MyList`)
just use them instead of writing your own.

    Test test = quacksLike(Collection.class).test(MyList.class);

If you provide Junit3-style `suite()` method, test can be run by junit test runner.

    @RunWith(AllTests.class)
    public class MyListTest {
      public static junit.framework.Test suite() {
          return junit(quacksLike(Collection.class).test(MyList.class));
      }
    }

You can combine tests into bigger test suites.

    public static junit.framework.Test suite() {
      return junit(newSuite("MyList ... ")
          .testThat(MyList.class, quacksLike(Collection.class))
          .testThat(MyList.class, quacksLike( ... ))
          .testThat(MyList.class, quacksLike( ... )));
    }

You can define your own contracts by implementing `org.quackery.Tester` interface.

    public interface Tester<T> {
      Test test(T item);
    }

`Tester` can produce `Test` being single `Case`.

      public static Tester<Class<?>> quacksLikeFinalClass() {
        return new Tester<Class<?>>() {
          public Test test(final Class<?> type) {
            return new Case(type.getName() + " is final") {
              public void run() {
                if (!Modifier.isFinal(type.getModifiers())) {
                  throw new QuackeryAssertionException("" //
                      + "\n" //
                      + "  expected that type\n" //
                      + "    " + type.getName() + "\n" //
                      + "  has modifier final\n" //
                  );
                }
              }
            };
          }
        };
      }

Or `Test` combine many tests as `Suite`.

      public static Tester<Class<?>> quacksLikeX() {
        return new Tester<Class<?>>() {
          public Test test(Class<?> type) {
            return newSuite(type + " quacks like X")
                .testThat(type, quacksLikeA())
                .testThat(type, quacksLikeB());
          }
        };
      }

### Asserting

Quackery is agnostic about what assertion library you use and what exceptions/errors you throw.
However all built-in testers throw `org.quackery.QuackeryAssertionException` in case of failed tests
and `org.quackery.QuackeryAssumptionException` in case when test cannot be run.
You are encouraged to copy that behavior.

Those exceptions are translated to native exceptions/errors when converting to other frameworks (like junit).

### Junit

Quackery `Test` can be converted to `junit.framework.Test` and run by junit test runner.

    @RunWith(AllTests.class)
    public class MyListTest {
      public static junit.framework.Test suite() {
          return junit(quacksLike(Collection.class).test(MyList.class));
      }
    }

Exceptions throws by quackery are translated to junit natives:
  - `org.quackery.QuackeryAssertionException` to `java.lang.AssertionError`
  - `org.quackery.QuackeryAssumptionException` to `org.junit.internal.AssumptionViolatedException`
