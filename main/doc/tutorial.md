[built-in contracts](#built-in-contracts) |
[defining you own contracts](#defining-your-own-contracts) |
[running](#running) |
[reporting](#reporting) |
[junit](#junit) |
[extra](#extra)

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
This means that if you implement your own collection, you don't have to write those tests at all.
Just write tests for functionality that makes your collection unique.

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

[Case](#case) | [Suite](#suite) | [Contract](#contract)

### Case

The most basic concept of quackery library is `Case`.
It is used to test smallest possible piece of functionality.

You can create case by extending `Case` class and overriding `run` method.

    Case test = new Case("empty string has length of zero") {
      public void run() {
        assertTrue("".length() == 0);
      }
    };

Or using `newCase` factory method that accepts testing logic in form of a lambda expression.

    import static org.quackery.Case.newCase;
    ...
    Case test = newCase(
        "empty string has length of zero",
        () -> assertTrue("".length() == 0));

`Case` has a human-readable name that is used in reports and a `run` method that encapsulates testing logic.
`Case` is considered successful if `run` method ends without throwing `Throwable`.
Any throwable indicates failed tests. However there are different ways `Case` can fail.

 - `org.quackery.report.AssertException` - feature does not work
 - `org.quackery.report.AssumeException` - feature depends on some other feature that does not work
 - other `Throwable` - any unexpected situation

`AssertException` and `AssumeException` contain methods that throw those exceptions on various conditions.
If you use other assertions library then read [junit section](#junit).

### Suite

`Test` interface mimics composite design pattern.
It has 2 subclasses.
`Case` represents single test that can either succeed or fail.
`Suite` represents list of tests (cases and suites).
This way you can organize your cases into hierarchical tree.

You can aggregate cases into suites using `Suite` fluent grammar.

    Suite suite = suite("all tests")
        .add(test(1))
        .add(test(2))
        .addAll(asList(test(3), test(4)))
        .add(suite("nested tests")
            .add(test(5))
            .add(test(6)))
        .add(test(7));

### Contract

`Contract` is a functional interface that represents reusable test.

    public interface Contract<T> {
      Test test(T item);
    }

It contains logic that tests functionality of an item, but it doesn't know what this item is.
Item is provided by client programmer.
Contract can be small, returning a single `Case`, or it can be bigger, returning `Suite` of tests.

Let's start with a simple contract that takes item of type `Object` and produces single `Case` that tests if this item is equal to itself.

```
import static java.lang.String.format;
import static org.quackery.Case.newCase;
import static org.quackery.report.AssertException.assertTrue;

import org.quackery.Contract;
import org.quackery.Test;

public class IsEqualToItself implements Contract<Object> {
  public Test test(Object item) {
    return newCase(
        format("%s is equal to itself", item),
        () -> assertTrue(item.equals(item)));
  }
}
```

Since `Contract` is a functional interface we can implement it as a method.

```
public class Contracts {
  public static Test isEqualToItself(Object value) {
    return newCase(
        format("%s is equal to itself", value),
        () -> assertTrue(value.equals(value)));
  }
}
```

Now we can obtain contract instance using method reference.

    Contract<Object> contract = Contracts::isEqualToItself;

Contracts are useful when building a suite of similar cases.

```
    suite("string is equal to itself")
        .addAll(asList("first", "second", "third"), Contracts::isEqualToItself);
```

will generate a suite of tests

```
string is equal to itself
  first is equal to itself
  second is equal to itself
  third is equal to itself
```

`Contract` allows you to built a library of reusable tests similar to built-in contracts.

# running

To run all tests in test tree, you would need to traverse the tree, find each `Case` and invoke `Case.run()`, then catch `Throwable` if test failed and prepare some kind of report. Luckily `org.quackery.run.Runners` provides methods for automating it. Additionally, it contains helper methods that allow you to control things like concurrency or test isolation.

The simplest way to run tests is calling `run(test)`. It runs each `Case` eagerly (which may take some time) and caches results. It returns a test identical to argument, with the same tree structure and names. The only difference is that invoking `Case.run()` on any case from the returned tree returns/throws cached result immediately.

### concurrency

To run tests concurrently call `concurrent(test)` method. It starts `Executor` that uses all available processors and schedules tasks for running each `Case`. While executor keeps working, method returns immediately (does not block). To block until executor finishes running tests, use `run(concurrent(test))`. If you don't like configuration of default executor you can provide you own calling `run(in(executor, test))` instead.

### isolation

All tests run in the same jvm and it's possible for them to share state and affect each other. Most of the time it's undesirable, so you want to isolate them.

Sometimes your project's production code (the code you test) loads bytecode dynamically. Running many tests that loads bytecode as a side-effect can cause problems. Bytecode loaded by one test can be visible to another test resulting in namespace collisions. This can be prevented by decorating tests using `classLoaderScoped(test)`. It makes each `Case` to have different context `ClassLoader` (original one being parent). We made an assumption here, that you load bytecode using `Thread.currentThread().getContextClassLoader()`. If you use custom loading policy (which you shouldn't!), isolating test from each other might be impossible.

Using `ThreadLocal` is popular way to avoid synchronization issues for static resources that don't need to be global (cache, network connections pool, etc.). If `ThreadLocal` reference is static, then running 2 tests using the same thread makes one test affecting the other. To isolate them use `threadScoped(test)` which makes each `Case` to be run in different thread. This does not make them run concurrently, because original thread joins new thread (blocks until new thread finishes).

# reporting

Once you run the test and cache results, you are ready to present report.

    Test report = run(test);

`org.quackery.report.Reports` contains methods related to analyzing results of test. Trying to use `Reports` on `Test` that was not run, will invoke `Case.run()` every time.

All tests passed if `count(Throwable.class, report)` returns `0`. You can also count number of failures of specific type, for example `count(AssertException.class, report)` or `count(AssumeException.class)`.

You can turn test results into `String` using `format(Test)`. String includes test names and structure of whole tree including throwables thrown from them.

# junit

To run quackery tests with junit use junit's `@RunWith` together with quackery's `org.quackery.junit.QuackeryRunner`.

    @RunWith(QuackeryRunner.class)
    public class ArrayListTest {
      @Quackery
      public static Test test() {
          return quacksLike(Collection.class)
              .test(ArrayList.class));
      }
    }

Define factory method and annotate it with `@Quackery`. Method must be `public`, `static`, have no parameters and return `Test` (or its subclass). You can define more than one method. All tests returned by those methods are aggregated in one root `Suite`.

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

There are many problems that may occur during initialization. Method annotated with `@Quackery` could have incorrect signature, have some parameters or throw exception during invocation. All such problems are caught and represented as failing tests. This way they can be reported together with other tests. 

### decorators

You are free to use all decorators described in [running section](#running) except `run(Test)`. Running tests manually will block junit thread (which is building test hierarchy) until tests finish. Result will still be correct but it will freeze junit runner UI and prevent displaying test progress in real time. 
If you want to run tests concurrently just use `concurrent(test)` decorator without wrapping it inside `run` method.

    @RunWith(QuackeryRunner.class)
    public class ArrayListTest {
      @Quackery
      public static Test test() {
          return concurrent(quacksLike(Collection.class)
              .test(ArrayList.class)));
      }
    }

### mixing with junit tests

`QuackeryRunner` adds possibility to run tests annotated with `@Quackery`.
It also keeps features provided by default junit4 runner.
This means that if you already have junit class with tests.

    public class ArrayListTest {
      @Test
      public void implements_random_access() {
        assertTrue(RandomAccess.class.isAssignableFrom(ArrayList.class));
      }
    }

You can add quackery tests inside and quackery runner will merge them into one hierarchy.

```
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
```

Default junit runner can detect problems during initialization too, missing default constructor for example. Those problems are caught. However, junit validation happens only if there is at least one method annotated with `@org.junit.Test`. This mean that if you use quackery methods exclusively, you don't even need to provide default constructor.

`@org.junit.Ignore` does not work on methods annotated with `@Quackery`. However ignoring whole class will ignore also quackery tests.

Watch out for name collision between `org.quackery.Test` and `org.junit.Test`!

# extra

 - [Eclipse templates](eclipse_templates.md)
