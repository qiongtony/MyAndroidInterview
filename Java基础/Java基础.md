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
