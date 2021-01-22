# final详解

private隐式地指定为final，不然为啥子类了一样的方法却不能覆盖呢？

```java
public class Base {
    private void test() {
    }
}

public class Son extends Base{
    public void test() {
    }
    public static void main(String[] args) {
        Son son = new Son();
        Base father = son;
        //father.test();
    }
}
```

final方法可以被重载

```java
public class FinalExampleParent {
    public final void test() {
    }

    public final void test(String str) {
    }
}
```

