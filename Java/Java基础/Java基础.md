# Java基础

## 一、面向对象

面向对象编程：一种编程范式或编程风格，以**类**和**对象**为组织代码的基本单元，将面向对象的特性（抽象、封装、继承、多态），作为代码设计和实现的基石（编程范式、类和对象为基本单元、距有面向对象特性）

Java是面向对象编程语言

面向对象的四个特性：

- 封装：给类套个“盒子”，将数据和操作隐藏起来，只暴露有限的访问接口，通过**访问权限控制**来支持（public都可访问、protected：同包类或子类访问、default：同包类访问、private：当前类内使用)。作用：提高代码的可读性和可维护性![](G:\ynote_save\20181227145354458)
- 抽象：如何**隐藏方法的具体实现，让调用者只需要关心方法提供了哪些功能**，实现方式：接口或抽象类，其实普通的（有点像告诉你我有这些功能，把主干告诉你）
- 继承
- 多态：父类引用指向子类对象

## 二、集合类

- List

- Set

- Map

### List

种类:

- ArrayList

- LinkedList

ArrayList与ListList比较:

1. 数组和链表的区别

2. ArrayList的扩容:

   时机:长度当前长度

   扩容的处理:

   ​		size = 原size + (原size) / 2

   ​		拷贝原位置到新数组
   
3. LinkedList实现了List和Deque接口，可以看作顺序容器，队列，和栈。

## Set

**无需唯一**

种类

- HashSet

- TreeSet

比较:

1. HashSet基于Hash算法实现,TreeSet基于红黑树实现,数据不大的情况下HashSet优于TreeSet
2. HashSet适用于快速查找,TreeSet适用于排序

- HashSet

  底层是HashMap

- LinkedHashSet

  底层数据结构是链表和哈希表，具有HashSet的查找效率，内部通过双向链表维护了插入顺序

- TreeSet

  底层数据结构是红黑树

## Queue/Deque

FIFO

- LinkedList

  双向链表
  
- ArrayDeque:

  循环数组实现，栈和队列都可以用它来实现

  扩容：

  ​		大小：原大小的两倍；

  ​		处理：依据head分成左右部分，先将head的右边部分移到新数组，再将head左边部分从左到右移到新数组

  优势：数组比链表相比，查找效率高吧

- ProrityQueue

  特性：每次取出的是权值最小的元素

  实现方式：小顶堆

## Map

- HashMap

![](https://www.yuque.com/flywith24/tree/about-me?inner=ud2dx)

- LinkedHashMap

  存储访问顺序，根据优先级


## 一些问题

**1、谈谈对Java平台的理解**

1. 面向对象（封装、继承、多态）
2. 平台无关性（字节码与JVM）tip：JVM不是解释执行的，有一些如HotSpot是JIT编译执行
3. 语言特性（泛型、Lambda）
4. 类库（集合、并发、网络、IO和NIO）
5. 内存回收

### 2、Exception和Error的区别，throws和throw的区别

Exception一般可以用来捕获，其中又分为可检查异常和不可检查异常

可检查异常：没有继承自RuntimeException的异常，如FileNotFoundException，在代码里必须显示地捕获处理，在编译器就能发现

不可检查异常：继承自RuntimeException的异常，很明显在运行时才能发现，如：NullPointerException，可以通过良好编码避免

Error：在正常情况下，不大可能出现的情况，绝大部分的Error都会导致程序处于非正常状态，所以一般不需要捕获或者说捕获也没法解决

throws：可能抛出的异常

throw：抛出异常

NoClassDefFoundError和ClassNotFoundException的区别

- 前者是Error，不应该捕获，后者是异常，一般可以捕获处理
- 前者是指编译时该类存在，在加载时却找不到该类的情况；后者一般是指在通过反射获取类时，类名书写错误导致的

### 3、equal和hashCode方法

equal默认与“==”一致，一般重写用来表示值相等

equal相等，hashCode一定也相等，反过来不一定

hashCode一般用于hashMap/hashSet等的寻址，当hashCode相等时才判断equal是否一致，达到降低equal调用提高效率的效果

equal的特性：

1. 自反性：x.equals(x)//true
2. 对称性：a.equals(b) == b.equals(a)
3. 传递性
4. 一致性：多次调用，结果一致
5. 非null对象，equals(null)返回false

### 4、参数传递

Java的参数传递是值传递，不是引用传递，对于引用对象，传递的是对象的地址值

> 引用和地址有啥区别

### 5、死锁产生的原因

- 互斥
- 互斥长期持有
- 循环依赖

如何避免？

银行家算法

> 当一个进程申请资源时，先试一下分配给它，然后根据安全性算法判判断分配后的系统是否安全，安全的话就分配给该进程，不安全则试探失败，该进程进入等待

多人贷款如何如何能够完成所有贷款（先尝试贷给你，还钱后其余贷款也能够完成就是成了，不行就是不能先贷给你）

![这里写图片描述](https://img-blog.csdn.net/20180508204335770?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMzNDE0Mjcx/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## finalize()

是什么？Object内的一个方法

作用：在GC收集对象前进行资源的回收

为什么不建议用：

GC时机不确定，需要释放的资源应该尽快释放；

需要对该对象单独处理，影响GC性能

### 更好的方式

Cleaner：使用幻象引用监听对象的回收