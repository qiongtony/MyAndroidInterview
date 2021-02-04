# BroadcastReceiver

- 场景/作用
- 静态注册和动态注册

## 场景/作用

接收系统内或应用内的全局通知（如手机启动、开始充电、网络状态变化）等

## 注册

静态注册：

使用步骤：

1. 创建自定义BroadcastReceiver的类，实现onReceive方法
2. 在AndroidManifest对该类进行注册，在Intent-Filter里声明注册的action
3. 收到系统广播/手动调用sendBroadcast，这时候就会在onReceive收到了

特点：

- 不需要在代码里注册
- 应用安装就可接收广播

动态注册：

1. 创建自定义BroadcastReceiver的类，实现onReceive方法
2. 在代码里创建Intent-Filter，声明广播的action
3. 创建BroadcastReceiver实例
4. 调用registerReceiver(BroadcastReceiver,IntentFilter)

## 广播发送方式

无序广播：

- 发送：sendBroadcast(...)
- 特点：无序，收到广播后无法进行拦截。效率比有有序广播高

有序广播

- 发送：sendOrderedBroadcast(...)

- 特点：根据AndroidManifest里的Intent-Filter指定的优先级prority，由高到低发送，相同优先级随机发送；可以通过调用abortBroadcast来中断广播；比起无序广播，效率比较低

本地广播

- 发送：使用LocalBroadcstManager实例的sendBroadcast(...)发送的广播
- 特点：
  - 本应用才能接收到（应该说是当前进程？）
  - 不需要跨进程，效率更高
  - 只能通过动态注册去注册广播

> onReceive运行在主线程，如果要执行耗时操作，需要调用goAsync获取PendingIntent，然后在子线程任务执行后调用penndingIntent.finish()方法，如果不获取PenddingIntent，则onReceiver执行后进程就可能被回收