# Java并发类的总结

分类

- 工具类
- 集合类（ConcurrentHashMap、CopyOnWriteArrayList）
- 阻塞队列
- 线程池

## 工具类

ReentrantLock(重入锁)

CountDownLatch(倒计时)

Semaphare(信号量)

CyclicBarrier(栅栏)

ReentrantReadWriteLock(读写锁)

Exchanger

LockSupport()

ReentrantLock与synchronized的区别

- ReentrantLock可重入锁，更灵活，手动调用lock加锁，unlock释放锁；synchronized自动加锁，释放锁
- ReentrantLock用AQS实现，synchronzied使用motinerenter、monitorexit指令实现
- ReentrantLock支持多个Condition实现等待-唤醒，具体是await()、signal()/signalAll()方法；synchronized使用Object的wait()、Object的notify/notifyAll实现等待-唤醒；
- ReentrantLock支持公平锁和非公平锁，默认为非公平锁，公平锁可以减少“饥饿”线程的出现；synchronized是非公平锁；

## AQS

AQS的核心：(CLH队列)

state：

- 抢占：state>0，重入锁的数量
- 共享：

为什么的实现是CAS+Volatile

CAS入队、出队

