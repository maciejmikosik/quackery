
### Built-in contracts

Some well-known contracts are already implemented by quackery.
Best example being contracts for `Collections` like `List` and `Set`.
Look into `Contracts` class for `quacksLike*` family of methods.

    import static org.quackery.Contracts.quacksLike;

Contracts are customizable so you can choose what features you expect from implementation.

    quacksLike(Collection.class)
        .implementing(List.class)
        .mutable()
        .test(java.util.ArrayList.class);

    quacksLike(Collection.class)
        .implementing(List.class)
        .withFactory("copyOf")
        .test(com.google.common.collect.ImmutableList.class);

### Junit

You can run quackery tests using junit by using `QuackeryRunner`.

    @RunWith(QuackeryRunner.class)
    public class MyListTest {
      @Quackery
      public static Test test() {
          return quacksLike(Collection.class)
              .implementing(List.class)
              .test(MyList.class));
      }
    }

### Building Suites

You can combine tests into bigger test suites.

    import static org.quackery.Suite.suite;

    @Quackery
    public static Test test() {
      return suite("MyCollection ... ")
          .testThat(MyCollection.class, quacksLike(Collection.class))
          .testThat(MyCollection.class, quacksLike( ... ))
          .testThat(MyCollection.class, quacksLike( ... ));
    }

### Defining you own contracts


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
            return suite(type + " quacks like X")
                .testThat(type, quacksLikeA())
                .testThat(type, quacksLikeB());
          }
        };
      }
