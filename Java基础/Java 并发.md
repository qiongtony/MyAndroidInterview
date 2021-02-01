# Java 并发

并发三要素引起的问题：

1. CPU增加了**缓存**->**可见性**问题
2. 操作系统增加了进程、线程，**线程切换**，分时复用CPU->**原子性**问题（显然是线程和分时复用导致的）
3. **编译程序优化**指令执行次序->**有序性**问题

可见性：一个线程对共享变量的修改，其他线程能立即看到

原子性：操作要么全部执行，要么都不执行。CPU只能保证CPU指令级别的操作的原子性->需要有保证高级语言上的原子性

有序性：程序执行顺序按照代码的先后顺序->JMM保证方法内存屏障

内存屏障的种类：（保证内存的可见性和禁止重排序）

1. LoadLoad：Load1,LoadLoad,Load2,Load1在Load2前执行完
2. StoreStore：Store1，StoreStore，Store2，Store2对Store1的写入可见
3. LoadStore
4. StoreLoad



## Java如何解决并发问题：JMM

Java内存模型（JMM)：规范了JVM按需禁用缓存和编译优化的方法。具体方法包括（三个关键字一个规则）

- volatile、synchronzied和final三个关键字

- Happens-Before规则


// 这段需要吗？

JMM对并发特性的具体支持：

- 原子性：只对数字的读写具有原子性

- 可见性：提供volatile关键字，保证修改的值会立即被更新到主存（为了保证原子性，该关键字用在数据类型内）；加锁
- 有序性：volatile关键字；加锁---JMM本质是通过Happens-Before规则来保证的。

> Happens-Before的含义：前面的操作的结果对后续操作是可见的（不仅是顺序，且包含可见性）

Happens-Before规则（针对的是可见性）

场景：

1. 程序顺序性规则：在一个线程中，前面的操作HB于后续的任意操作
2. 管程锁定：一个unlock操作先行发生于后面对同一个锁的lock操作（保证加锁、解锁的可见性）
3. volatile变量规则：volatile变量的写操作先于后面对这个变量的读操作
4. 传递性：A HB B，且B HB C，则A HB C
5. Thread对象的start()方法调用先于此线程的每一个动作（保证线程正常启动）
6. Thread对象的结束先于join方法返回

### volatile

作用：禁用缓存，可见性（写HB读）

### synchornized

获取锁时的三种情况


## 解决原子性问题

>  问题本质：保证中间状态对外不可见

使用锁，保证同一时刻只有一个线程执行

共享数据按照线程安全程度的强弱顺序分成五类：

1. 不可变

   - final修饰的基本数据类型

   - String（不可变类）

   - 枚举

   - Number部分子类

   - 用Collections.unmodifiableXxx()方法返回的不可变集合

     为啥这种很安全呢？首先肯定是原子性了，不能修改肯定也是符合的可见性，有序性通过final关键字保证？

2. 绝对线程安全

3. 相对线程安全：保证对这个对象**单独**的操作是线程线程的（内部的顺序是安全的）

   - 线程安全类

## 线程互斥同步

锁机制：

> 公平锁：多个线程再等待同一个锁时，按照锁申请顺序来依次获得锁

1. JVM实现的synchronized

   可修饰类、类实例，用在方法和方法块内

   锁是非公平的

2. JDK实现的ReentrantLock

   默认也是非公平的，也可以是公平的

   可同时绑定多个Condition对象

线程之间写作：

Thread内join：在b内调用a.join，保证a执行先于b后面的执行。**无需在同步方法/同步控制块中**

Object内：wait、notify、notifyAll

wait线程释放锁被挂起，等到其他线程调用notify/notifyAll才会被唤醒

特点：必须在加锁内部使用（同步方法/同步控制块），否则会报**运行时IllegalMonitorStateException**

为什么要释放锁：针对notify，必须在相同锁下，为了避免死锁必须先释放锁

代码实例如下：虽然b先运行，但由于join的原因会等到A线程结束才继续执行

```java
public class JoinDemo {
    public static void main(String[] args){
        A a = new A();
        B b = new B(a);
        b.start();
        a.start();
    }

    private static class A extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("A start");
            for (int i = 0; i < 1000; i++){
                System.out.print(i + " ");
                if (i % 100 == 0){
                    System.out.println();
                }
            }
            System.out.println("A end");
        }
    }

    private static class B extends Thread{

        private A a;

        public B(A a){
            this.a = a;
        }

        @Override
        public void run() {
            super.run();
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("B");
        }
    }
}
```

wait()和sleep()的区别：

- wait是Object的方法，sleep是Thread的静态方法

- wait会释放锁，sleep不会

## 线程安全的实现方法
1. 互斥同步
    synchronized和ReentrantLock

2. 非阻塞同步

### synchronized关键字

作用：加锁，保证可见性和原子性（不保证有序性）

原理分析

通过javac生成.class文件，然后用javap反编译class文件，我们看到锁机制下有两个关键指令---**Monitorenter**和**Monitorexit**，前者表示想获取锁，后者表示释放锁

**Monitorenter**指令执行时有三种情况：

1. monitor计数器为0，表示目前未被获取，这个线程会立刻获得然后锁计数器+1，别的线程无法再获取，只能等待；

2. 已拿到，又重入，锁计数器会累加；

3. 锁被别的线程获取，等待锁释放

精简版：
- 锁的mointor是0，锁计数器+1，获得锁，其他线程无法再获取，等待

- 不为0

  ​		1. 本线程获取了该锁，属于重入，锁计数器+1；

  ​		2.锁被别的线程获取了，等待锁释放；

Monitorexit：锁计数器-1，如果此时为0，释放锁；不为0，说明是重入，当前线程继续持有

synchronized的注意点：

1. 锁对象不能为空，锁的信息保存再对象头里
2. 作用域不宜过大，影响性能
3. 避免死锁
4. 能不用的情况下，用线程安全类替代实现

### 非阻塞同步

互斥同步属于悲观锁---不加锁就会有问题

非阻塞的实现方式：

1. CAS指令

   含义：先进行操作，共享资源不存在争夺，操作成功；否则采取补偿措施---不断重试

   特点：通过硬件支持，支持的最经典原子操作是：比较并交换

2. AtomicInteger

   特点：保证原子操作，其实用的就是CAS的思想，对不上的时候一直重试

3. ABA

   为了解决AtmoicInteger的问题：初始是A，改为了B，然后再改回A，此时会认为没有改动过

   如何解决的，变量值带版本

### 无同步方案

1. 栈封闭：（线程私有）方法的局部变量
2. 线程本地存储：作用域为线程，典型：ThreadLocal![](https://www.pdai.tech/_images/pics/3646544a-cb57-451d-9e03-d3c4f5e4434a.png)
3. 可重入代码