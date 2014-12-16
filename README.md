Testanza lets you write reusable tests by implementing simple interface.

    public interface Tester<T> {
      Test test(T item);
    }

Then you can build test suite for your particular class.

    testThat(MyList.class, obeysCollectionContract());
    testThat(MyList.class, obeysListContract());
    testThat(MyList.class, isAssignableTo(Serializable.class));

This will create a hierarchy of test for your class.

![MyListTest.png](main/doc/MyListTest.png "MyListTest.png")

You can use built-in `Testers` or create your own.

You can aggregate smaller `Testers` into more abstract contracts.

See [tutorial](main/doc/tutorial.md) for all features.
