Binder

指什么：

- 通过来说，Binder指的是一种IPC机制
- 对于Server进程来说，Binder指的是Binder本地对象
- 对于Client来说，Binder指的是Binder代理对象

内存分为：内核态和用户态，每个进程都在各自的用户态里->通过内核态中介

为什么需要IPC？

1. 进程隔离->需要一个公共空间，即内核

2. 内存分为用户空间和内核空间（进程共享），对于32位操作系统，内核为1G->出于安全性考虑，有些操作很危险，不能让你随便弄啊

3. 系统调用分为用户态和内核态（用户空间访问内核空间的**唯一**方式）->运行在哪个空间，就属于哪个态

   内核态：当一个任务执行系统调用而陷入内核代码中执行时，称进程处于内核态

   ​	系统调用的实现：

   ​	copy_from_user：将数据从用户空间拷贝到内核

   ​	copy_to_user：将数据从内核空间拷贝用户空间

   用户态：当进程在执行用户自己的代码的时候，称进程处于用户态

为什么不用Linux提供IPC机制？

IPC方式 | 拷贝次数
--- |---
Socket / 管道/消息队列 | 2次（发送方缓存->内核->接收方缓存） 
Binder | 1次
共享内存 | 0次

性能：

Socket作为通用接口，传输效率低、开销大；管道/消息队列采用存储-转发方式，需要拷贝两次；共享内存无需拷贝，控制复杂，难以使用。

安全性：

1.1传统IPC接收方获取不到发送方的UID/PID->没办法进行身份鉴别

1.2传统IPC在用户数据包填入UID/PID->不可靠，容易被篡改或利用

2. 传统IPC访问接入点是开放的->无法阻止恶意程序通过猜测接收方地址获得连接（能够破解）->Binder支持实名和匿名

不用系统提供的IPC，那Android的Binder如何实现IPC呢？动态加载内核模块LKM（独立编译，不能独立运行，运行时链接到内核作为内核的一部分，在Android该通信模块即为Binder驱动）和mmap()（内核和用户空间建立映射，两者对其修改都能同步，这样省去一次拷贝，只要把数据拷到内核就行了），前者解决了Binder无法运行在内核实现跨进程的问题，后者是Binder性能比其他IPC方式快的原因

Client保存有Server的代理对象，请求时根据Proxy向SM发送请求，SM让Server的真实对象执行返回结果，SM将结果返回给Client（这里不需要Client拷贝到SM,SM拷贝到Server这样四次拷贝的意思？）

通信过程如下图：

![图片](https://mmbiz.qpic.cn/mmbiz_png/I9ZtCicgN42La4rW1FZW6OUhlBmppNXwp3kc9RycRFPlSkgxJUoC3YH0qGcbXzJHNHX6cDicnWDKpdIlFAIjnicNw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

> 疑问：为什么要建立两次映射呢？

## IBinder/IInterface/Binder/BinderProxy/Stub

分别代表什么

- IBinder是一个接口，代表一种跨进程传输的能力
- Binder类，代表的是Binder本地对象
- IInterface：具体的通信
- BinderProxy：远程代理对象
- Stub：AIDL帮我们生成的Binder本地对象，具体的执行逻辑要自己实现

Binder驱动



Client、Server、SM、Binder驱动

Client、Server和SM位于用户态

Binder驱动位于内核态

Server在SM注册时IPC是如何执行的？系统事先创了一个0Binder实例用来做这个事

## Binder协议

基本格式：命令+数据

交互：ioctl(fd, cmd, arg)函数

命令：参数cmd

数据：参数arg

TF_ONE_WAY：交互是同步/异步，异步的话不返回任何数据，不需要

## Binder 跨进程通信原理

Binder不是系统提供的IPC，是如何实现的？通过Linux的动态内核可加载模块LKM（可独立编译，不可独立运行）。运行时链接到内核作为内核的一部分运行，Android系统通过这个内核模块作为桥梁与用户进程进行通信(这个在内核的模块就是Binder驱动)

Ok解决跨进程通信问题，那通信的方式是跟传统的IPC一样吗？（发送方数据拷到内核，内核拷到接收方）

使用Linux的内存映射方式，具体是指mmap()方法

> 内存映射：用户空间的一块区域映射到内核，用户空间或内核修改另一方都能同步反应

好处：减少数据拷贝次数