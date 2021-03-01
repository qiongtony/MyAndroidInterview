# Java并发类的总结

分类

- 工具类
- 集合类
- 阻塞队列
- 线程池

# 工具类

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

# 二、集合类

1. ConcurrentHashMap
2. CopyOnWriteArrayList

## 2.1ConcurrentHashMap

出现的缘由：HashMap不是线程安全的，在并发情况下put操作丢失的情况，HashTable采用的是存和取时整体加锁，性能不够高效

特点：

- Concurrent-Level代表Segment的数量，默认是16->默认可以有16个线程并发访问？
- 使用分段锁
- 不允许key/value为null

JDK1.7与JDK1.8的不同实现

## JDK1.7

数据结构：采用**Segment**+**HashEntry**,如下图![img](https://upload-images.jianshu.io/upload_images/2038379-86fa87bc78f712b7.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/500/format/webp)

Segment继承自ReentrantLock，这里的方式叫分段锁，其实就是有多个Segment的意思吧？！加锁是给Segment加锁，不会互相影响

理论上支持CurrencyLevel（Segment数量2的幂）的线程并发

### get操作

只需要保证可见性，不需要同步操作

### put操作

```java
public V put(K key, V value) {
        Segment<K,V> s;
        if (value == null)
            throw new NullPointerException();
        // 二次哈希，以保证数据的分散性，避免哈希冲突
        int hash = hash(key.hashCode());
        int j = (hash >>> segmentShift) & segmentMask;
        if ((s = (Segment<K,V>)UNSAFE.getObject          // nonvolatile; recheck
             (segments, (j << SSHIFT) + SBASE)) == null) //  in ensureSegment
            s = ensureSegment(j);
        return s.put(key, hash, value, false);
    }

final V put(K key, int hash, V value, boolean onlyIfAbsent) {
            // scanAndLockForPut 会去查找是否有 key 相同 Node
            // 无论如何，确保获取锁
            HashEntry<K,V> node = tryLock() ? null :
                scanAndLockForPut(key, hash, value);
            V oldValue;
            try {
                HashEntry<K,V>[] tab = table;
                int index = (tab.length - 1) & hash;
                HashEntry<K,V> first = entryAt(tab, index);
                for (HashEntry<K,V> e = first;;) {
                    if (e != null) {
                        K k;
                        // 更新已有 value...
                    }
                    else {
                        // 放置 HashEntry 到特定位置，如果超过阈值，进行 rehash
                        // ...
                    }
                }
            } finally {
                unlock();
            }
            return oldValue;
        }
 
```

首先二次哈希获取到Segment->Segment加锁插入到HashEntry

### size

首先，获取到的不是准确的size，因为计算size时锁定所有Segment成本非常高，使用的是重试机制（RETRIES_BEFORE_LOCK=2），如果两次计算出的size没有变化，就直接返回，否则获取锁进行操作

## JDK1.8

为什么要改进：查询遍历链表效率太低（需要两次hash）,Segment太复杂了

数据结构：Node+CAS+Synchronized

CAS：用于实现创建Node节点的原子操作

put操作

```java
	int hash = spread(key.hashCode());
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            // i是hash后求余得到的下标
            Node<K,V> f; int n, i, fh;
            // 1、首次put，初始化table
            if (tab == null || (n = tab.length) == 0)
                tab = initTable();
            // 2、该桶没有节点，直接创建节点
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                if (casTabAt(tab, i, null,
                             new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            // 3、转发节点
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else {
                // hash冲突，加内部锁
                V oldVal = null;
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        // 链表，插入到链表尾部
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;
                                if ((e = e.next) == null) {
                                    pred.next = new Node<K,V>(hash, key,
                                                              value, null);
                                    break;
                                }
                            }
                        }
                        // 红黑树
                        else if (f instanceof TreeBin) {
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                        else if (f instanceof ReservationNode)
                            throw new IllegalStateException("Recursive update");
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);
        return null;
```

1. 第一次put，初始化table数组，执行2；
2. 该下标的Node为空，通过CAS创建链表；
3. 进行扩容；
4. 存在hash冲突，给该下标的Node加锁，如果是Node，则插入到链表尾部，如果是红黑树，插入新的节点
5. 检查链表长度是否大于8，大于则将链表转化为红黑树

ConcurrentHashMap的优化方式，Node+CAS+Synchronized，创建Node操作为CAS，哈希冲突时加锁后进行处理

### size

通过遍历CounterCell数组，求出长度，CounterCell基于Long.Adder，保证自增自减的原子性，所以获取到的size也不是准确的

##  2.2CopyOnWriteArrayList

特点：

1. 添加/删除元素，操作加锁，且操作后生成新的数组

2. get操作不加锁

场景：由于每次增删操作需要进行拷贝，操作比较重->读多写少的情况

# 三、阻塞队列

LinkedBlockingQueue

ArrayBlockingQueue

## LinkedBlockingQueue

实现：

CAS+ReentrantLock+单向链表

线程A要取出数据时，队列为空，线程A执行notEmpty.await()进行等待；当其他线程B添加数据后，会调用notEmpty.signal唤醒线程A，此时线程A会被唤醒将数据取出。且在线程A取数据前takeLock进行加锁，取完后释放锁

线程A要放入数据时，队列满了，线程A执行notFull.await()进行等待；当其他线程B取出数据后，会调用notFull.signal唤醒线程A，线程A继续放入数据。放入数据前用takeLock加锁，操作后释放锁

两个ReentrantLock，以及它们的Condition，putLock对应notFull；takeLock对应notEmpty

特点：一般认为为无界队列，用链表实现

## ArrayBlockingQueue

实现：

CAS+ReentrantLock+数组

放入数据->满->notFull.lock->唤醒->放入数据->notEmpty.signal

取出数据->空->notEmpty.lock->唤醒->取出数据->notFull.signal

特点：有界队列，查找快

## SynchronousQueue 

特点：

用于线程间交换数据，不能缓存任何元素，放入元素时会阻塞，直到取出元素为止

## 总结

边界：Array有界；Linked无界；Synchronous不缓存

空间利用：Array比Linked更紧凑，但初始内存更大；

吞吐量：Linked比Array更好->锁操作更细粒度

性能：Array比Linked好，实现比较简单

