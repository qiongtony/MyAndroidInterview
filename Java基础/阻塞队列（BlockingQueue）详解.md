# 阻塞队列（BlockingQueue）详解

# 问题

- 什么是阻塞队列？

- BlockingQueue有哪些实现类

- BlockingQueue的适用场景

- 常用方法

- 插入方法有哪些？分别有什么区别

- BlockingDeque和BlockingQueue的关系

  Queue是队列，Deque是双端队列

- BlockingDeque适合的场景

- BlockingDeque有哪些实现类

## 一、什么是阻塞队列

存储任务的队列，在生产者-消费者中，存储生产者的任务，为消费者返回任务![img](https://www.pdai.tech/_images/thread/java-thread-x-blocking-queue-1.png))

如何实现阻塞呢？ReentrantLock

## ReentrantLock

- 什么是可重入，什么是可重入锁？它用来解决什么问题？

  类似Synshcorinzed那样已获得锁可以重复获取锁，state会累加，执行完后会锁数量会减少，直到为0表示锁已释放?

- ReentrantLock的核心是AQS，那么它怎么来实现的？说说其类内部结构关系

  主要有三个了Lock、FairLock和UnfairLock

  Lock实现了公共的逻辑，加锁和释放，FairLock是公平锁，UnfairLock是非公平锁，公平是指锁释放后是否根据等待时间的长度去获取锁对象。

- ReentrantLock是如何实现公平锁的？

  按照锁申请的先后顺序去获取锁

- ReentrantLock是如何实现非公平锁的？

  任意一个等待的线程都可能获取到锁

- ReentrantLock默认实现的是公平还是非公平锁？非公平锁

- 使用ReentrantLock实现公平和非公平锁的示例？根据构造函数，true为公平锁，false为非公平锁

- ReentrantLock和Synchronized的对比？

  比synchornized更安全

  等待可中断

  支持多个条件：这个怎么理解？就是锁内还有其他条件，持有的条件和释放的条件要全部抵消才能继续执行

- ReentrantLock的适用场景？

  

> AQS：同步器，

