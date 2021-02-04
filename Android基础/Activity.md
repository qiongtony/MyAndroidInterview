# Activity

- 作用

- 生命周期及典型场景

- launchMode

- 数据存储

- Intent

  - 显示Intent和隐式Intent

  - Intent-Filter规则

## 一、作用

包含用户界面的组件，主要用于和用户交互

## 二、生命周期及典型场景

![生命周期](https://developer.android.com/guide/components/images/activity_lifecycle.png)

onCreate->onStart(可见->onResume(可触摸->onPause(可见->onStop（后台->onDestroy

启动一个Activity

onCreate->onStart->onResume

按Home回到桌面

onPause->onStop

销毁一个Activity

onPause->onStop->onDestroy

Activity A 启动Activity B：

A.onPause->B.onCreate->B.onStart->B.onResume->A.onStop

[官网文档](https://developer.android.com/guide/components/activities/activity-lifecycle#onresume)

横竖屏切换：

onPause->onStop->onDestory->onCreate->onStart->onResume

**活动状态**：

1. 运行状态：Activity位于栈顶，可见

2. 暂停状态：不在栈顶，仍可见

3. 停止状态：不位于栈顶，完全不可见

4. 销毁状态：Activity从返回栈中移除

Activity状态以及系统终止进程的可能性的关系：
系统终止进程的可能性 | 进程状态 | Activity状态
---|---|---
较小|前台（拥有或即将获得焦点） | 已创建、已开始、已恢复
较大 |后台（失去焦点） | 已暂停
最大 | 后台（不可见）、空| 已停止、已销毁

Activity状态->进程状态->终止进程的可能性

### LaunchMode

Activity实例存在任务栈里,任务栈分为前台和后台区别

StandardMode：标准模式，默认模式，维护了一个栈，后进先出

SingleTop：当前任务的栈顶是否为该Activity实例，是的话，不创建新实例，调用oNewIntent方法；否则，创建Activity实例，压入栈内

SingleTask：SingleTop的进化版本，如果其他任务栈内有该实例,不断出栈，直到实例为栈顶,并将该实例指向新的任务栈；没有创建Activity实例，创建新任务,压入栈内

> 如果在前台的Activity启动后台任务里存在的模式为SingleTask的Activity实例,系统会将后台任务整个转到前台运行
>
> ![前台任务与后台任务的切换](https://developer.android.com/images/fundamentals/diagram_backstack_singletask_multiactivity.png)

SingleInstance：与SingleTask类似,任务栈内只有一个该Activity的实例

Intent方式:

- FLAG_ACTIVITY_NEW_TASK:表现与SingleTask一致
- FLAG_ACTIVITY_SINGLE_TOP:表现与SingleTop一致
- FLAG_ACTIVITY_CLEAR_TOP:如果要启动的Activity已在任务中,不创建新实例,从任务不断出栈,直到该实例为栈顶为止.最常与FLAG_ACTIVITY_NEW_TASK结合使用

运用场景：

首页设为SingleTask，方便退出、踢出

闪屏页用SingleTop

## 数据的保存

轻量数据：

在onSaveInstanceState：保存状态

在onRestoreInstanceState/onCreate且Bundle不为空时：恢复状态

执行顺序：

onPause->onSaveInstanceState

onCreate->onStart->onRestoreInstanceState

onRestore与onCreate的区别

- onCreate里的Bundle新建实例时为空，重建不为空，onRestore的Bundle不为空

- onRestore只在重建时才会被调用

JetPack方式:

ViewModel

- 特点:在配置更改重建时数据仍然存在,保存在内存中,不需要进行序列化
- 缺点:在系统回收已停止的Activity时数据被回收了(这种情况是内存不足被回收的情况?)

## Intent

### 显示Intent和隐式Intent

显示：

构造参数传入Context和要启动的四大组件的Class

隐式：

不传入具体的Class，根据action和category来查找符合条件的组件

只能指定一个action，category可以指定多个

如何找到对应的四大组件？

action->0个或一个

category->0~N个（activity默认有DEFAULT的category，这样看应该是1~N？）

data：Uri的相关数据，需要全部一致才算匹配