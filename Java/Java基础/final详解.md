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

static final是编译期常量，final不一定

```java
public class Test {
    static Random r = new Random();
    // 非编译期常量
    final int k = r.nextInt(10);
    // 编译器常量
    static final int k2 = r.nextInt(10);

    public static void main(String[] args){
        Test t1 = new Test();
        System.out.println("k = "  + t1.k + " k2 = " + t1.k2);
        Test t2 = new Test();
        System.out.println("k = "  + t2.k + " k2 = " + t2.k2);
    }
}
```

打印：

```
k = 7 k2 = 8
k = 9 k2 = 8
```

## final域重排序规则

前提：处理器和编译器为了优化性能会对指令序列进行重排序

而final对该重排序进行了一定的约束，分为基本数据类型和引用类型

### final域为基本类型

写final域重排序规则：final的写通过插入storestore屏障禁止重排序到构造函数之外

读final域重排序规则：读该对象的引用和读该对象包含的final域禁止重排序---防止引用没获取就读引用里面的final（但这种明显就不可能出现吧，一旦出现不是就出错了？）

### final域为引用类型

写final域：在基本类型基础上增加一条：构造函数对final域的写入，与构造函数外final域引用赋给一个引用变量不能重排序（简单说就是要先让final域写了再给外面的引用）

读操作：没有增加

总结：

- 基本数据类型：
  - final域写：禁止final域写与构造方法重排序（不能还没初始化就用）
  - final域读：禁止初次读对象的引用与读该对象包含的final域的重排序（还是读到默认值）