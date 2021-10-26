Java泛型

为什么要引入泛型，省掉强转，未引入前只能List<Object>这样创建list

泛型擦除：

编译期擦除类型信息，所以是伪泛型：类型不是真正存在的，泛型不能实例化

```java
		List<String> stringList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
		// 结果为：stringList.class == integerList.class ? true，说明没有List<String>这个类型，都是List
        System.out.println("stringList.class == integerList.class ? " +
                (stringList.getClass() == integerList.getClass()));
```



父类的泛型不是子类的泛型的父类：List<Human> list = List<Man>x

获取泛型的类型——泛型类的子类(更简单的方式，匿名内部类)

```java
	Class<IntPair> clazz = IntPair.class;
        Type t = clazz.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type[] types = pt.getActualTypeArguments(); // 可能有多个泛型类型
            Type firstType = types[0]; // 取第一个泛型类型
            Class<?> typeClass = (Class<?>) firstType;
            System.out.println(typeClass); // Integer
        }

    public static class IntPair extends Pair<Integer> {
        public IntPair(Integer first, Integer last) {
            super(first, last);
        }
    }
```



协变：? extends Human，List<Human> list = List<Man>

​	只能读

逆变：? super Man

​	只能写

List<?>=List<? extends object>

List<*> = List<* super object>

PECS

​	Producer(P)：向外界提供数据，能作为形参和实参

​	Consumer(C)：处理数据，只能作为形参

tips:静态方法的泛型声明，不能使用类的泛型，原因：普通方法通过对象实例获取到了具体类型，而静态方法没有对象实例，所以需要声明

```java
public class Pair<T> {
    private T first;
    private T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }
    
    // 普通方法可使用泛型T
    public T getFirst() {
        return first;
    }
    
    /*
     报错Error:(12, 38) java: 无法从静态上下文中引用非静态 类型变量 T
    public static Pair<T> createPair(T first, T second){
        return new Pair<T>(first, second);
    }
     */

    // 正确方式
    public static <T> Pair<T> create(T first, T second){
        return new Pair<T>(first, second);
    }
}
```



