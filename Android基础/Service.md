# Service

- 作用/场景
- 启动方式
- 生命周期

## 一、作用/场景

执行后台任务/轮询任务/不需要UI的任务

前台服务

后台服务

## 二、启动方式

startService（多次start只会有一个实例，只需要一次stop）

bindService

关闭方式：

startService：可多次start，一次关闭

bindService：可多次bind，绑定的对象都需要调用unbind才会关闭

两者都有的情况：

需要符合两者关闭的条件后才能关闭

## 三、生命周期

![生命周期](https://developer.android.com/images/service_lifecycle.png)

startService:onCreate->onStartCommand->

关闭：stopSelf或者stopService

bindService:onCreate->onBind

销毁：onDestory

bind销毁->onUnBind

onStartCommand的返回值

- 含义：系统终止服务情况下如何继续运行服务
- 三种返回值：
  - START_NOT_STICKY：系统不会重建服务，最安全的选项
  - START_STICKY：重建服务并调用onStartCommand，但不会重传最后一个Intent---Intent为空。适用于不执行命令，等待作业的媒体播放器
  - START_REDELIVER_INTENT：重建服务并调用onStartCommand，重传最后一个Intent。所有挂起Intent均依次传递（挂起Intent是什么意思）。适用于主动执行应立即恢复的作业或者说实时更新？

与Service进行通信：

onBind方式，Service里的onBind方法返回IBinder实例，客户端创建ServiceConnection在onServiceConnected将IBinder强转为Service声明的类型

![混合方式下onBind返回值的影响](https://developer.android.com/images/fundamentals/service_binding_tree_lifecycle.png)

## 相应Service类的介绍

IntentService

- 特点：运行在子线程执行任务，任务逐一执行（在onHandIntent方法处理)，任务结束后会自动销毁Serivce
- 场景：后台任务

## 应用

**Messenger**

应用场景：相同进程内的通信

使用步骤：

1. 在Service端创建一个自定义的Handler接收客户端消息，通过传入Handler构造Messenger的实例
2. 在onBind根据messenger.getBinder()返回IBinder实例
3. 创建ServiceConnection实例，在onServiceConnect将IBinder传入Messenger的构造参数生成Messenger实例
4. 客户端调用bindService绑定Service
5. 通过messenger.sendMessage(Message)发送消息给Service端

AIDL:不同进程内的通信

使用步骤：

1. 创建.aidl文件
2. 在Service类内实现aidl生成文件的Servcie.Stub类
3. 在Service类的onBind返回IBinder
4. 客户端实现ServiceConnection，在onServiceConnect中构造Service实例通过Stub.asInterface(IBinder)
5. 客户局调用aidl声明的方法