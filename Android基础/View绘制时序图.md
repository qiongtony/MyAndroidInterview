setContentView的过程

```mermaid
sequenceDiagram
	Activity ->> Activity : setContentView
	Activity ->> + PhoneWindow : setContentView
	PhoneWindow ->>  PhoneWindow : installDecor
	PhoneWindow ->> PhoneWindow : gengerateDecor
	PhoneWindow ->>  PhoneWindow : generateLayout
	PhoneWindow ->> - LayoutInflater : inflate(layoutResId,mContentParent)
```

generateLayout:

初始布局的xml文件R.layout.screen_simple，设置layoutResId放在id为R.id.content的FrameLayout内

总结：在PhoneWindow进行setContentView，包含初始的布局，利用LayoutInflater解析xml文件，得到View，并添加到contentParent内，只是将布局文件解析到Java内未进行绘制

## 二、绘制

绘制入口:ActivityThread.handleResumeActivity

经过WindowMangerImpl和WMGlobal后，最终通过新建的ViewRootImpl，设置DecorView，执行requestLayout请求绘制

```mermaid
sequenceDiagram
	ActivityThread ->> + ActivityThread : performLaunchActivity
	ActivityThread ->> ActivityThread : handleResumeActivity
	ActivityThread ->> ActivityThread : performResumeActivity
	ActivityThread ->> - WindowManagerImpl : addView(decor,l)
	WindowManagerImpl ->> + WindowManagerGlobal : addView
	WindowManagerGlobal ->> - WindowManagerGlobal : new ViewRootImpl
	WindowManagerGlobal ->>  ViewRootImpl : setView
	ViewRootImpl ->> ViewRootImpl : requestLayout()
```

真正的绘制核心：

```mermaid
sequenceDiagram
	ViewRootImpl ->> ViewRootImpl : requestLayout()
	ViewRootImpl ->> + ViewRootImpl :  scheduleTraversals()
	ViewRootImpl ->>  MessageQueue : postSyncBarrier
	ViewRootImpl ->> - Choreographer : postCallback
	Choreographer ->> ViewRootImpl : doTraversal
	ViewRootImpl ->> + ViewRootImpl : performTraversals
	ViewRootImpl ->> ViewRootImpl : performMeasure
	ViewRootImpl ->> View : measure
	ViewRootImpl ->> ViewRootImpl : performLayout
	ViewRootImpl ->> View : layout
	ViewRootImpl ->> ViewRootImpl : performDraw
	ViewRootImpl ->> - View : draw
```

