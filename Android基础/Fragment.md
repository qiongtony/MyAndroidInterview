# Fragment

- 生命周期
- FragmentManager
- Fragment事务
- 返回栈

## 一、生命周期

onCreate->onBind->onActivityCreated->onCreateView->onStart->onResume->onPause->onStop->onDestroyView->onUnBind

## 二、FragmentManager

Activity/Fragment能获取到的FM，Activity通过supportFragmentManager获取到自己的FM，Fragment通过parentFragmentManager获取到宿主的FM，通过childFragmentManager获取自己的FM

![每个宿主都有与其关联的 FragmentManager，用于管理其子 Fragment](https://developer.android.com/images/guide/fragments/manager-mappings.png)