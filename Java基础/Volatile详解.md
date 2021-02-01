# Volatile详解

相关的问题：

1. volatile关键字的作用
2. volatile能保证原子性吗
3. 32位机器上共享的long和double变量为什么要用volatile？64位机器上是否也需要
4. i++为什么不能保证原子性？
5. volatile如何实现可见性？内存屏障
6. volatile如何实现有序性？happens-before规则等
7. volatile的应用场景

## volatile作用

主要两个作用：

- 有序性：防止重排序
- 可见性：

### 防重排序

经典例子，双重检查加锁为什么要加volatile关键字

```java
public class Singleton {
    public static volatile Singleton singleton;
    /**
     * 构造函数私有，禁止外部实例化
     */
    private Singleton() {};
    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (singleton) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
```

关键点实例化对象可分为三个步骤：

- 分配内存空间（弄个缸）
- 初始化对象（把东西整出来）
- 将内存空间地址赋值给对应的引用（把找到东西的纸条装进缸里）

由于**重排序**，上述过程可能变成如下过程：

- 分配内存空间
- 将内存空间地址赋值给对应的引用
- 初始化对象

在多线程环境下，该流程会将未初始化的对象引用暴露出来，导致不可预知的结果，所以需要加volatile

### 实现可见性

### 不能保证原子性，只保证单次读写操作原子性

这个不太理解，本身不是原子性的吗？在32位机器上，double/long分为高32位和低32位，读/写可能不是原子的---64位是原子的

## volatile的实现原理

> volatile变量的内存可见性是基于内存屏障
>
> 内存屏障：禁止重排序（感觉答非所问？

处理器不直接和内存进行通信，而是将内存数据拷一份到缓存后在进行处理，操作完后不是立即写到内存

volatile修饰的变量写后立即更新到内存->其他处理器**嗅探**总线上的数据交换，发现缓存地址被修改内->设置该缓存失效->从内存拿更新缓存

> 缓存一致性
>
> 大多都属于“嗅探”协议
>
> 嗅探协议：
>
> 1. 所有内存传输发生在一条共享总线上，所有处理器都能看到
>
> 2. 每个处理器缓存是独立的，内存共享->处理器通过“仲裁”将缓存读写到内存;
>
> 3. 每个处理器在不断嗅探总线的数据交换，一个处理器去读写内存其余处理器都会收到通知，保持缓存同步。
>
> 其实就是观察者模式，每个观察者还有一份拷贝，先修改备份，然后同步   
>

### 有序性实现

happens-before规则和禁止重排序

happens-before规则中有一条volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读

