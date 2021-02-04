# Glide

Glide.with(...)

做的事情：

1. Glide.get(...):通过反射生成GeneratedAppGlideModule对象实例，获取Glide单例

Glide如何检测Fragment/Activity的生命周期，来防止内存泄漏的？

```java
RequestManagerRetriever.with(Fragment fragment)
生成SupportRequestManagerFragment监听Activity/Fragment的生命周期回调
这里每个Fragment都有一个列表？FragmentManager用的是Fragment的getChildFragmentMananger
通过在Fragment添加一个子Fragment来监听Fragment的生命周期->控制请求的处理、
从这里也可以看出RequestManager->生命
Activity同理
```

图片加载框架需要考虑的问题：

- 异步加载

  需要用到线程池，完成后需要切换回主线程Handler

- 缓存

  三级缓存：内存->磁盘缓存->网络，实现方式LruCache

- 处理OOM

  软引用

  避免内存泄漏：Glide通过监听Activity/Fragment的生命周期来处理线程池中的任务列表

  onLowMemory：Glide此时会清空内存缓存、bitmap缓存、ArrayPool的缓存

  Bitmap加载方式：选用RGB_565，4.7+使用ARGB_4444

- 列表加载问题

RequestOptions和Glide.ovveride这样的方式是一样的吗？

是一样的，传入options会批量的去改

返回的文件类型没办法在options里设置

asBitmap/asFile，进行分类

url设置不了- -

listener可以在这设吗？不可以

改为用options后：简化了选项的

想处理的应该是把url的重复放到同一个地方，没办法自定义设置RequestManager= 。=

