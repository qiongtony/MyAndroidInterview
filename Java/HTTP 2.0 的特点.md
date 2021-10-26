HTTP 2.0 的特点 
- 二进制
- 头压缩HACK算法
- 默认长连接
- 多路复用 （还有吗） 
- 服务端主动响应

OKHttp 缓存的实现 
kotlin apk vs java apk 包大小 
APK 编码流程 

- kotlin > java 

- java + so > 字节码 

- 动态码 > dex 

- so > 资源 
  动态代理 vs 静态代理? 
  

安卓动画 

	- 帧动画 
	
	- 补间动画 
	- 属性动画

TextureView vs SurfaceView 3 

- SurfaceView 子窗口，独立的buffer，独立的画布，不在一个 view 体系，独立子线程 

- TextureView 是一个 view，效率低，比较容易变化，布局 render 线程中，慢大概3帧 
  常驻线程 3 

- 主线程 

- render 

- gc 

- jvm deamon 

- 默认 1个，最多15个 binder线程 

fragment 加载到可见的速度优化？

- 布局优化
- 不做耗时操作
- 延迟加载（资源如图片在可见后才加载）---Handler.addIdleHandler?
- 异步加载？
- 如果是vp+fragment降低缓存个数，防止同时加载的framgent数量