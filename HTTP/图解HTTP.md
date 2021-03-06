# 图解HTTP

Web浏览器输入URL到Web页面呈现的具体过程是什么？

首先Web是建立在HTTP协议上的通信---协议是指规则的约定

最初HTTP只是用在Web框架上进行文本传输的，现在扩展了各种应用场景，如文本、zip、二进制、json等

为了理解HTTP，由于需要了解TCP/IP协议族，HTTP是该协议族的一个子集

TCP/IP的分层：应用层、传输层、网络层和数据链路层。分层的原因：每层只做每层的事，修改后不会影响到其他层的功能

- 应用层

  向用户提供应用服务

  - FTP：文件传输协议

  - DNS：域名系统

  - HTTP

- 传输层

  提供处于网络连接中两台计算机之间的的数据传输（数据怎么传，可靠还是不可靠，怎么处理超时）

  - TCP：传输控制协议
  - UDP：用户数据报协议

- 网络层

  处理在网络上流动的数据包

  选择传输的路线

  - IP

- 数据链路层

TCP

DNS

> 负责域名解析

为什么要有这个？通过纯数字的IP地址不利于记忆，所以提供域名供用户访问，传输时通过DNS将域名解析为IP

URI和URL

URI是Uniform Resource Indentifier(统一资源标识符)是某个协议方案表示的资源的**标识符**（如:http,ftp等方案的标识符），URL是Uniform Resource Locator(统一资源定位符,如“http://www.baidu.com/")，定位符属于标识符的一种，所以URL是URI的 子集

URI：某个协议方案资源的标识符，告诉我们这个协议的资源怎么声明的，如"ftp://ftcp.is.co.za/rfc/rfc1808.txt"

## HTTP协议简单讲解

HTTP协议的无状态是指什么？

不对请求和响应做持久化处理（发过就忘了）

请求方法：

- GET：获取资源，不带body
- post:
- PUT：获取资源，带body
- DELATE：删除资源
- HEAD：获取响应头，查看URI是否正确

那如何处理登录后记住登录状态的需要呢？

Cookie（客户端保存，发送时自动携带，set-Cookie服务端返回，给客户端更新Cookie)

## HTTP请求和响应报文

内容协商，首部字段：

- Accept
- Accept-Charset
- Accept-Encoding
- Accept-Language
- Content-Language

## HTTP状态码

 |类别|解释
---|---|---
1XX|信息性状态码|请求正在处理
2XX|成功| 
3XX|重定向|需要进行附加操作以完成请求
4XX|客户端错误|服务器无法处理请求
5XX|服务器错误|服务器处理请求出错

通信数据转发程序：代理、网关、隧道

代理：客户端->代理->源服务器，代理做转发功能，用的还是相同的协议，经过代理的HTTP请求会增加VIA字段

网关：客户端->网关->源服务器，网关与代理的区别是，网关是将客户端的A协议请求转换为B协议请求发送给源服务器

隧道：客户端->隧道加密->源服务器，隧道的作用是确保客户端和服务器通信的安全

## HTTP首部

通用首部：请求和响应报文都可以有的

- Cache-Control

  public/private：该缓存可使用的范围

  no-cache：不缓存过期的资源

  no-store：本地和服务器不存储

  max-age：客户端包含该指令，表示只接收比指定时间小的缓存；服务器返回的响应包含，缓存服务器不对资源有效性确认，max-age数值代表资源保存为缓存的最长时间

  only-if-cached：客户端只收缓存服务器的缓存，缓存服务器无响应，返回504

- Connection

  作用：

  - 不转发的首部字段

  - 管理持久连接（连接还是断开,Keep-Alive Or Close）

- Date

  创建报文的日期和时间

请求首部

响应首部