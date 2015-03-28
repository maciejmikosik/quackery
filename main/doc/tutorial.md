
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

You can define your own contracts by implementing `org.quackery.Contract` interface.

    public interface Contract<T> {
      Test test(T item);
    }

`Contract` can produce `Test` being single `Case`.

      public static Contract<Class<?>> quacksLikeFinalClass() {
        return new Contract<Class<?>>() {
          public Test test(final Class<?> type) {
            return new Case(type.getName() + " is final") {
              public void run() {
                assertThat(Modifier.isFinal(type.getModifiers()));
              }
            };
          }
        };
      }

Or `Test` can combine many tests as `Suite`.

      public static Contract<Class<?>> quacksLikeX() {
        return new Contract<Class<?>>() {
          public Test test(Class<?> type) {
            return newSuite(type + " quacks like X")
                .testThat(type, quacksLikeA())
                .testThat(type, quacksLikeB());
          }
        };
      }

### Built-in contracts

Built-in contracts are designed to obey some rules.
They throw `org.quackery.AssertionException` if test fails.
Sometimes executing test makes no sense because some more basic feature already covered by other test failed.
In that situation `org.quackery.AssumptionException` is thrown.

Those exceptions are wrapped by native exceptions/errors
when tests are run by runner from other framework (like junit).

### Junit

You can run quackery tests using junit by using `QuackeryRunner`.

    @RunWith(QuackeryRunner.class)
    public class MyListTest {
      @Quackery
      public static Test test() {
          return quacksLike(Collection.class).test(MyList.class));
      }
    }
