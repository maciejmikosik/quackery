[built-in contracts](#built-in-contracts) |
[defining you own contracts](#defining-your-own-contracts) |
[running](#running) |
[integration](#integration)

# built-in contracts
[collection contract](#collection-contract)

Quackery lets you define your own contracts containing reusable tests,
but it also includes some tests for popular contracts.
It's the same way [hamcrest](http://hamcrest.org/JavaHamcrest/) let's you write your own `Matcher`,
while having rich library of most common ones.

For an easy start let's first take a look at contracts that are already included.
To make built-in contracts available add following import.

    import static org.quackery.Contracts.quacksLike;

### collection contract

Quackery has built-in contracts for various types of `Collection`.
This means that if you implement your own collection, you don't have to write any tests at all.

Collection contracts are customizable so you can choose what features you expect from implementation.

 - `implementing(List.class)` - collection implements `List` interface
 - `withFactory("factoryMethodName")` - instead of having default and copy constructors, collection has factory method named `factoryMethodName`
 - `immutable()` - collection does not support mutator methods
 - `forbidding(null)` - collection cannot contain `null` elements

Example usage looks like this.

    Test test = quacksLike(Collection.class)
        .implementing(List.class)
        .test(java.util.ArrayList.class);

    Test test = quacksLike(Collection.class)
        .implementing(List.class)
        .immutable()
        .forbidding(null)
        .withFactory("copyOf");
        .test(com.google.common.collect.ImmutableList.class);

Generated tests can be run using junit runner.

    @RunWith(QuackeryRunner.class)
    public class ArrayListTest {
      @Quackery
      public static Test test() {
          return quacksLike(Collection.class)
              .test(ArrayList.class));
      }
    }

Or you can run them with quackery native mechanism.

    import static org.quackery.report.Reports.format;
    import static org.quackery.run.Runners.run;
    ....
    Test test = quacksLike(Collection.class)
        .test(ArrayList.class);
    System.out.println(format(run(test)));

This would print test hierarchy formatted similar to junit's runner.

```
java.util.ArrayList quacks like collection
  implements Collection interface
  provides default constructor
    is declared
    is public
    creates empty collection
  provides copy constructor
    is declared
    is public
    can create collection with 1 element
    fails for null argument
    makes defensive copy
    does not modify argument
    allows null elements
  overrides size
    returns 0 if collection is empty
    returns 1 if collection has 1 element
  overrides isEmpty
    returns false if collection has 1 element
    returns true if collection is empty
  overrides contains
    returns false if collection does not contain element
    returns true if collection contains element
  overrides iterator
    traverses empty collection
    traverses singleton collection
```

In case of failure, test names are preceded by name of `Throwable` thrown from test.
`ArrayList` does not forbid `null` elements, so running this

    Test test = quacksLike(Collection.class)
        .forbidding(null)
        .test(ArrayList.class);
    System.out.println(format(run(test)));

prints this

```
java.util.ArrayList quacks like forbidding null collection
  implements Collection interface
  provides default constructor
    is declared
    is public
    creates empty collection
  provides copy constructor
    is declared
    is public
    can create collection with 1 element
    fails for null argument
    makes defensive copy
    does not modify argument
    [AssertException] forbids null elements
  overrides size
    returns 0 if collection is empty
    returns 1 if collection has 1 element
  overrides isEmpty
    returns false if collection has 1 element
    returns true if collection is empty
  overrides contains
    returns false if collection does not contain element
    returns true if collection contains element
  overrides iterator
    traverses empty collection
    traverses singleton collection
```

which shows failed test `[AssertException] forbids null elements`.

Tests list is definitely not complete, but it grows with each release.

# defining your own contracts

You can define your own contracts by implementing `org.quackery.Contract` interface.

    public interface Contract<T> {
      Test test(T item);
    }

`Contract` is generic, but in most cases it is used to test `Class<?>`.

`Test` mimics composite design pattern.
It has 2 subclasses.
`Case` represents single test that can either succeed or fail.
`Suite` represents list of tests (cases and suites).
This way you can organize your cases into hierarchical tree.

Let's define contract for class that tests only one thing: that class has `final` modifier.

    import static org.quackery.report.AssertException.assertTrue;
    ....
    public static Contract<Class<?>> quacksLikeFinalClass() {
      return new Contract<Class<?>>() {
        public Test test(final Class<?> type) {
          return new Case(type.getName() + " is final") {
            public void run() {
              assertTrue(Modifier.isFinal(type.getModifiers()));
            }
          };
        }
      };
    }

As shown above, you do it by extending `Case` class.
You need to provide a name, that will be visible in reports.
And you need to override `run` method, that contains testing logic.

If you use java 8, you can use factory method that accepts lambda expression.

    import static org.quackery.Case.newCase;
    ...
    newCase(type.getName() + " is final", () -> {
      assertTrue(Modifier.isFinal(type.getModifiers()));
    });

`Case` is considered successful if `run` method ends without throwing `Throwable`.
Any throwable indicates failed tests. However there are different ways `Case` can fail.

 - `org.quackery.report.AssertException` - feature does not work
 - `org.quackery.report.AssumeException` - feature depends on some other feature that does not work
 - other `Throwable` - any unexpected situation

`AssertException` and `AssumeException` contain methods that throw those exceptions on various conditions.
If you use other assertions library then read [integration section](#integration).

If you want your contract to produce more than single `Case` then aggregate them in `Suite` object.
It has methods for bulk operations and applying other contracts.

    public static Contract<Class<?>> quacksLikeX() {
      return new Contract<Class<?>>() {
        public Test test(Class<?> type) {
          return suite(type + " quacks like X")
              .add(test)
              .addAll(tests)
              .add(type, quacksLikeA())
              .addAll(types, quacksLikeB());
        }
      };
    }

# running

You can run `Case` manually by invoking `Case.run()`.
However you need to handle possible `Throwable` on your own.
It's more convenient to run any `Test` using `Runners` class.

    import static org.quackery.run.Runners.run;
    ....
    Test test = ...
    Test report = run(test);

`Runners.run(Test)` runs each `Case` eagerly (which may take some time) and caches its results.
It returns a report, that reuses the same interface as `Test`/`Case`/`Suite`.
The difference is that invoking `Case.run()` on any case from report returns/throws cached result immediately.

`org.quackery.run.Runners` contains methods related to running tests

 - `Test run(Test)` - runs each `Case` in same `Thread`
 - `Test runIn(Executor, Test)` - runs each `Case` in specified `Executor`
 - `Test threadScoped(Test)` - wraps test, so each `Case` is run in separate `Thread`, thus isolating `ThreadLocal` variables
 - `Test classLoaderScoped(Test)` - wraps test, so each `Case` is run in separate `ClassLoader`, thus isolating dynamically loaded bytecode

`org.quackery.report.Reports` contains methods related to reports of run tests

 - `int count(Class<? extends Throwable>, Test)` - counts how many cases thrown specified throwable or its subclass
 - `String format(Test)` - turns test result into `String` including test names and throwables thrown from them

Trying to use `Reports` on `Test` that was not run, will invoke testing logic every time.

# integration

### Junit

To run quackery tests with junit use junit's `@RunWith` together with quackery's `org.quackery.junit.QuackeryRunner`.

    @RunWith(QuackeryRunner.class)
    public class ArrayListTest {
      @Quackery
      public static Test test() {
          return quacksLike(Collection.class)
              .test(ArrayList.class));
      }
    }

Define factory method and annotate it with `@Quackery`.
Method must be `public`, `static`, have no parameters and return `Test` (or its subclass).
If there is more than 1 annotated method, then tests returned by them are aggregated in ad hoc `Suite`.

If test throws one of quackery exceptions (like tests from built-in contracts do)
and you run this test using junit,
then those exceptions are translated to junit's native exceptions.

 - `org.quackery.report.AssertException` is translated to `java.lang.AssertionError`
 - `org.quackery.report.AssumeException` is translated to `org.junit.AssumptionViolatedExcetpion`
 - any other `Throwable` passes through

 If test throws non-quackery exception (like `AssertionError` thrown by `org.junit.Assert`),
 then this exception passes through quackery and reaches junit's runner.
 Thus, if you use junit's assertions in combination with junit's runner, then you are fine.
 Otherwise, you are responsible to make sure your runner and assertions library are compatible.

`QuackeryRunner`, while adding possibility to run tests annotated with `@Quackery`, keeps all features provided by default junit4 runner.
This mean that if you already have junit class with tests.

    public class ArrayListTest {
      @Test
      public void implements_random_access() {
        assertTrue(RandomAccess.class.isAssignableFrom(ArrayList.class));
      }
    }

You can add quackery tests inside.

    @RunWith(QuackeryRunner.class)
    public class ArrayListTest {
      @Quackery
      public static org.quackery.Test test() {
        return quacksLike(Collection.class)
            .test(ArrayList.class);
      }

      @Test
      public void implements_random_access() {
        assertTrue(RandomAccess.class.isAssignableFrom(ArrayList.class));
      }
    }

Quackery merges all test into one hierarchy. There are few things to keep in mind.

 - Watch out for name collision between `org.quackery.Test` and `org.junit.Test`
 - `@Ignore` does not work on methods annotated with `@Quackery`.
     However it works if you use it at class level.
 - You can use `@QuackeryRunner` even on class that only have junit tests.
     This makes no difference except one detail.
     It does not complain if you have no test methods in class, like default junit4 runner does.
