# Wws_Android_Interview

面向的复习计划以及相关知识点的总结

预计：1个半月

## 目录

**Java基础**：（一周改为三天1.20-12.22）（一周1.20-1.27）

- 面向对象的特性
- 集合类
- 并发（synchroinzed锁机制参考Hencoder Plus？线程安全的集合类）

增加项目：

- ThreadLocal的实现原理

- 生产者-消费者模型

- 线程池的实现方式

**Jvm相关**（参考《深入理解JVM》）（两天1.23-1.24）(实际1.27-1.31）

- 垃圾回收机制
- 分代回收算法
- 垃圾回收算法
- 类加载过程（双亲委托）

补充：

- JMM（Java内存结构模型）

**Android基础**（三天1.25-27，参考第一行代码及官方文档）

- 四大组件

  Activity的作用、生命周期（典型场景下生命周期的调用顺序）、launchMode

  Service的作用、两种启动模式（startService和bindService）

  BoradcastReciver的作用、两种注册方式（静态注册和动态注册）

  ContentProvider的作用

- 数据库的基本使用（SQLiteHelper）和基础语法（增删改查）

- View的绘制流程

- 事件分发机制以及滑动冲突的两种解决方式

- 消息循环机制（Handler、MessageQueue、Looper、Message）

**JetPack**（三天1.28-1.30，这个没有深入到原理还是不好说）

- LiveData
- Databinding(ViewAdapter)
- LifeCycle
- ViewModel

**Android进阶**（两周1.31-2.9，参考书及本章并整理“口水话”）

- 相关的源码

  - 系统启动流程

  - 应用进程启动流程

  - 四大组件启动流程

  - WMS/AMS作用
-  性能优化（比较薄弱）
-  启动优化（比较薄弱）

**第三方框架源码**（三天）

- Glide

- OkHttp+Retrofit

- RxJava

- EventBus

- BaseRecyclerViewAdapter

**设计模式**（两天）

- 单例（几种的优缺点）
- 代理模式
- 观察者模式
- 建造者模式
- 抽象工厂

**算法**（一周，预计刷150题）

- 栈
- 数组
- 队列
- 哈希表
- 树
- 排序
- 动态规划
