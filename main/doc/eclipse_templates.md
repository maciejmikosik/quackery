# Eclipse templates

Creates new class annotated with `@RunWith(QuackeryRunner.class)`. Defines single method annotated with `@Quackery` returning empty suite.

```
@${rw:newType(org.junit.runner.RunWith)}(${qr:newType(org.quackery.junit.QuackeryRunner)}.class)
public class ${primary_type_name} {
  @${q:newType(org.quackery.Quackery)}
  public static ${test:newType(org.quackery.Test)} test() {
    return ${staticImport:importStatic(org.quackery.Suite.suite)}suite(${primary_type_name}.class.getName())${cursor};
  }
}
```