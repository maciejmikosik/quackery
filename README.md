Quackery lets you test if your code quacks like intended type.

Add this example class to you project.

    @RunWith(QuackeryRunner.class)
    public class JdkCollectionTest {
      @Quackery
      public static Test test() {
        return newSuite("jdk collections quack like java.util.Collection")
            .testThat(ArrayList.class, quacksLike(Collection.class))
            .testThat(LinkedList.class, quacksLike(Collection.class))
            .testThat(HashSet.class, quacksLike(Collection.class));
      }
    }

Run it with junit test runner to see something like this.

![JdkTest.png](main/doc/JdkTest.png "JdkTest.png")

You can use built-in contracts or create your own.

See [tutorial](main/doc/tutorial.md) for all features.
