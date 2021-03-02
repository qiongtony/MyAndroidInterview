## 实现原理
> 桶+链表+红黑树实现，以key的hash值作为桶的序号

## put方法
源码：
```java
 /**
     * 将指定key与value关联起来
     * 如果map已有该key，则替换
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    
    // hasCode的hash值，在速度、实用性、质量上的综合考虑
    static final int hash(Object key) {
        int h;
        // 高16位与低16位进行异或操作
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                 boolean evict) {
      Node<K,V>[] tab; Node<K,V> p; int n, i;
      // 判断有无初始化，没有则初始化
      if ((tab = table) == null || (n = tab.length) == 0)
          n = (tab = resize()).length;
      // 判断该桶的链表有无节点，没有则创建节点      
      if ((p = tab[i = (n - 1) & hash]) == null)
          tab[i] = newNode(hash, key, value, null);
      else {
          Node<K,V> e; K k;
          // 该key已存在
          if (p.hash == hash &&
              ((k = p.key) == key || (key != null && key.equals(k))))
              e = p;
              // 如果是红黑树，使用红黑树查找方式
          else if (p instanceof TreeNode)
              e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
          else {
              // 在链表查询节点，1、新节点；2、重复节点
              for (int binCount = 0; ; ++binCount) {
                // 链表到头了，说明是新节点，创建并结束遍历
                  if ((e = p.next) == null) {
                      p.next = newNode(hash, key, value, null);
                      // 节点大于7，使用红黑树实现
                      if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                          treeifyBin(tab, hash);
                      break;
                  }
                  // key的hash值以及key相等，退出遍历
                  if (e.hash == hash &&
                      ((k = e.key) == key || (key != null && key.equals(k))))
                      break;
                  p = e;
              }
          }
          // 存在该key，由于onlyIfAbsent为false，覆盖旧value
          if (e != null) {
              V oldValue = e.value;
              if (!onlyIfAbsent || oldValue == null)
                  e.value = value;
              afterNodeAccess(e);
              return oldValue;
          }
      }
      ++modCount;
      // 超过cap*factor，需要进行扩容，cap的两倍
      if (++size > threshold)
          resize();
      afterNodeInsertion(evict);
      return null;
    }
```
疑问：hash值取的为什么不是对象的hash，而是高位和低位进行抑或操作

首先直接用hash值范围很大，但hash是和长度-1进行&操作求出下标的，所以根本用不到那么大范围的信息->冲撞更容易发生

概况一下问题：

- 直接用hash值范围大，实际用不到那么多

解决办法：高位和低位进行抑或，混合了高位和低位，加大了低位随机性

![img](https://pic2.zhimg.com/80/4acf898694b8fb53498542dc0c5f765a_720w.jpg?source=1940ef5c)

> 解决哈希冲突的方法：
>
> 线性再散列：这个坑占了，看下一个坑，有坑蹲就行
>
> 开放地址法：这个hash冲突了，重新hash一次

总结步骤：

1. 根据key的hashCode计算hash
2. 没有碰撞，直接放到桶里；
3. 碰撞且是链表，将新节点查到尾部；
4. 碰撞且链表过长，转换为红黑树
4. 节点已存在，覆盖旧Value；
5. 碰撞且是红黑树，红黑树查询；
6. 判断是否需要扩容；


## get方法
```
    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }
    
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
```
1. 根据hashCode计算hash值；
2. 查询桶的位置，如果是第一个节点，直接返回；
3. 如果是红黑树，查找节点，时间复杂度O(lgN);
4. 如果是链表，查找节点，时间复杂度O(N);

## 3.resize()扩容方法
源码如下：
```
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            // 大于2^30次方，设为2倍会导致越界，所以设为Int的最大值
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            // 扩容为原size的两倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // 设为初始值16
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            // 判断扩容新的最大位是0还是1，如果是0则还在原位置，如果是1则在原位置+oldCap的位置
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        // 新的最大位是1，位置修改，这样分布也相当于均匀了
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
```