
### Quick glance

Quackery contains pre-written test suites for well-known contracts.
If you are implementing your own collection (let's say `MyList`)
just use them instead of writing your own.

    Test test = quacksLike(Collection.class).test(MyList.class);

You can run this test by junit test runner.

    @RunWith(QuackeryRunner.class)
    public class MyListTest {
      @Quackery
      public static Test test() {
          return quacksLike(Collection.class).test(MyList.class);
      }
    }

You can combine tests into bigger test suites.

    @Quackery
    public static Test test() {
      return newSuite("MyList ... ")
          .testThat(MyList.class, quacksLike(Collection.class))
          .testThat(MyList.class, quacksLike( ... ))
          .testThat(MyList.class, quacksLike( ... ));
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
                  throw new FailureException("" //
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

Or `Test` can combine many tests as `Suite`.

      public static Tester<Class<?>> quacksLikeX() {
        return new Tester<Class<?>>() {
          public Test test(Class<?> type) {
            return newSuite(type + " quacks like X")
                .testThat(type, quacksLikeA())
                .testThat(type, quacksLikeB());
          }
        };
      }

### Built-in testers

Built-in testers are designed to obey some rules.
They throw `org.quackery.FailureException` if test fails.
Sometimes executing test makes no sense because some more basic feature already covered by other test failed.
In that situation `org.quackery.QuackeryAssumptionException` is thrown.

Those exceptions are wrapped by native exceptions/errors
when tests are run by runner from other framework (like junit).

### Junit

Quackery does no provide native running mechanism.
`org.quackery.Test` can be run by `org.quackery.QuackeryRunner` which implements `org.junit.runner.Runner`.
Method returning `Test` you want to run must be public, static, have no parameters and be annotated with `org.quackery.Quackery`.

    @RunWith(QuackeryRunner.class)
    public class MyListTest {
      @Quackery
      public static Test test() {
          return quacksLike(Collection.class).test(MyList.class));
      }
    }

Exceptions thrown by quackery are translated to junit natives:
  - `org.quackery.FailureException` to `java.lang.AssertionError`
  - `org.quackery.QuackeryAssumptionException` to `org.junit.internal.AssumptionViolatedException`
