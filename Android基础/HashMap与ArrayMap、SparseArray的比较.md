HashMap与ArrayMap、SparseArray的比较

ArrayMap是Android专门针对内存优化而设计的

存储结构：

ArrayMap是两个数组：

- mHashes记录所有key的hashCode的int数组

- mArray记录key-value的数组，大小是mHashes的2倍

SparseArray也是两个数组：

- int数组存储int类型的key
- Object存储value

内存优化：

- ArrayMap比HashMap更省内存，在数据量不大的情况下，使用ArrayMap
- SparseArray比ArrayMap节省1/3的内存，但只能存储key为int类型的map，在该情况下优先使用SparseArray

性能：

- ArrayMap查找时间复杂度O（lgN），增删需要移动成员，速度较慢，对于个数小于1000的情况，性能基本没有差异
- HashMap查找、修改的时间复杂度为O（1）
- SparseArray适合频繁删除和插入的场景