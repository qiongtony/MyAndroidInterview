# Handler全面解析

- 同步屏障
- 阻塞唤醒机制（nativePoll

# 二、唤醒机制

句柄：标识符，获得对象的句柄，就可以对对象进行任意操作。（比指针更安全，封装了一层，方便系统去处理这个操作是否符合权限，可不可以做）

基于Linux的epoll，IO多路复用机制

natviePollOnce->调用epoll_wait方法会阻塞一段时间或epoll的fd中注册的fd发生了IO，时间到后Native层会写入fd，从而使得监听该fd的pollOnce方法被唤醒

nativeWakeJava层主动去唤醒