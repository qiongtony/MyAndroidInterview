# Handler全面解析

- 消息机制（收发）：Handler、Looper、MessageQueue

- 同步屏障：msg分为三类，同步消息、异步消息（async为true）、同步屏障（msg.target为空null）
- 阻塞唤醒机制（nativePollOnce）
- ThreadLocal

# 二、唤醒机制

句柄：标识符，获得对象的句柄，就可以对对象进行任意操作。（比指针更安全，封装了一层，方便系统去处理这个操作是否符合权限，可不可以做）

基于Linux的epoll，IO多路复用机制

natviePollOnce->调用epoll_wait方法会阻塞一段时间或epoll的fd中注册的fd发生了IO，时间到后Native层会写入fd，从而使得监听该fd的pollOnce方法被唤醒

nativeWakeJava层主动去唤醒

## 相关问题

为什么不会卡住？

​	nativePollOne会休眠，有消息在唤醒

ThreadLocal如何保证变量生命周期为线程内

​	thread里有个map，把变量value存在里面了

同步屏障

​	发起方：ViewRootImpl

​	作用：让渲染优先级提高，防止卡顿

Handler如何收到消息

​	msg.target为发送的Handler对象

在本线程内Handler、Looper、MessageQueue的比例

​	n：1：1

IdleHandler

​	