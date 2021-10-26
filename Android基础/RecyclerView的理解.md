RecyclerView的理解

回收池：

- 预加载下一屏的池子
- 被回收的池子

四级缓存：

​	mAttachScrap/mCacheScrap：layout时临时使用，根据pos可以直接拿来用

​	mCacheView：缓存滑动时即将可见的VH，一样根据pos直接拿来用，默认size是2；

​	mViewExtensions：一般不用

​	mRecyclerViewPool：根据viewType分类，每类默认缓存5个，需要调用onBindViewHolder重新进行数据绑定

涉及的主要方法：

​	tryGetViewHolderForPositionByDeadLine：从四级缓存一级级去取，如果都没取到，调用createViewHolder并绑定数据

​	removeAndRecycleViewAt：回收VH

​	fill：