 为什么要多线程？

提高资源利用率

线程和进程的区别

进程：操作系统调度的基本单位

线程：进程操作的基本单元

并发问题的原因：

JMM(Java内存模型将内存分为主内存和工作内存，线程将变量从主内存拷贝到自己的工作内存进行处理)->
可见性

CPU分时间片调度线程->原子性

编译器为了优化进行指令重排序->有序性

如何解决：

synchronized关键字和volatile

synchronized解决的问题：

原子性、可见性、有序性

修饰方法、代码块、静态方法，对对象加锁，包围的范围叫临界区

字节码：mointerenter、mointerexit，加锁、释放锁

汇编：lock cpmxchg

锁优化：

锁存在在哪里，类内存结构里对象头的markword（64位8字节

无锁

偏向锁：只有一个锁，没有竞争，markword记录下线程指针，执行完后如果指针没变，操作成功

自旋锁：多个线程竞争，往markword通过CAS写lock record(LR)指针，操作成功获取到锁，失败重复尝试，自旋操作不涉及内核态的切换，消耗CPU资源

重量级锁：自旋超过10次或竞争的线程数大于可用CPU的一半，向操作系统请求加锁，获取锁失败的会进入等待状态，不消耗CPU资源

锁降级：

GC，基本没啥用

锁消除

volatile：

有序性、可见性

有序性：单例的DCL问题

new包含三个指令：

1、为类分配内存；

2、初始化对象；

3、地址指向内存；

正常是123，如果在多线程情况下可能出现132的结果，导致获取到未初始化的对象

volatile如何解决：JMM（禁止指令重排序）

可见性：缓存一致性协议

写操作会立马同步到主内存，其余线程嗅探该变量的缓存会失效，从主内存更新工作内存



其余处理并发问题的工具

CAS：保证变量操作的原子性

AtomicXxx类

期望值，更新值

获取期望值的拷贝，查看拷贝是否与当前值是否一致，一致则执行修改，否则重复此步骤；

汇编：cmpexh

存在的问题：

ABA问题

解决：AtomicStampedReference

期望值+版本来判断

java提供的并发类

工具类：ReentrantLock、Seamphere、CyclicBarrier、CountdownLatch倒计时、AbrastQueueSynchronizer、ReentrantReadWriteLock、LockSupport

集合：ConcurrentHashMap、ConcurrentHashSet、CopyOnWriteArrayList

阻塞队列：LinkedBlockingQueue、ArrayBlockingQueue、SynchorousBlockingQueue

线程池：ThreadPoolExecutor

ThreadPoolExecutor

corePoolSize、maxiumPoolSize、keepLiveTime+TimeUnit、workQueue(BlokingQueue）、threadFactory、？？？

execute的执行流程：

1、线程数小于corePoolSize，addWorker

2、线程属不小于corePoolSize，队列未满，入队；

3、队列满了，线程数小于最大线程数，addWorker；

4、大于最大线程属：AbortPolicy：默认抛出异常

核心方法：

addWorker：Worker继承Runnable接口，持有线程实例，创建Worker实例，插入队列，执行线程的start，最终会执行runWokrer，不断从workQueue取出任务，并执行，如果没有任务，则线程销毁；

