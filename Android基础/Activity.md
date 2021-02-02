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

onCreate->onStart(可见->onResume(可触摸->onPause(可见->onStop（后台->onDestroy

启动一个Activity

onCreate->onStart->onResume

按Home回到桌面

onPause->onStop

销毁一个Activity

onPause->onStop->onDestroy

Activity A 启动Activity B：

A.onPause->B.onCreate->B.onStart->A.onStop->B.onResume

横竖屏切换：

onPause->onStop->onDestory->onCreate->onStart->onResume

**活动状态**：

1. 运行状态：Activity位于栈顶，可见
2. 暂停状态：不在栈顶，仍可见
3. 停止状态：不位于栈顶，完全不可见
4. 销毁状态：Activity从返回栈中移除

### LaunchMode

StandardMode：标准模式，默认模式，维护了一个栈，后进先出

SingleTop：判断栈顶是否为该Activity实例，是的话，不创建新实例，调用oNewIntent方法；否则，创建Activity实例，压入栈内

SingleInstance：SingleTop的进化版本，如果栈内有该实例，不断出栈，直到实例为栈顶；没有创建Activity实例，压入栈内

SingleTask：栈内只有一个该Activity的实例

运用场景：

首页设为SingleInstance，方便退出、踢出

闪屏页用SingleTop

## 数据的保存

在onSaveInstanceState：保存状态

在onRestoreInstanceState/onCreate且Bundle不为空时：恢复状态

执行顺序：

onPause->onSaveInstanceState

onCreate->onStart->onRestoreInstanceState

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