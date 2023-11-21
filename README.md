## 学习笔记博客的应用网站后台系统

### 开发者

@[朱远](https://gitee.com/zhu-yuanthorn11z/)<br/>
@[郑声军](https://gitee.com/zhengshengjun/)
<div>
<h1> ZSJ-BLOG</h1>
<p>加个好友一起讨论加油吧！</p>
</div>


---

## 🎉 特性

- 💪 [zsj_common](zsj_common)  公共模块
- 💅 [zsj_system](zsj_system)   系统模块
- 🌍 [zsj_article](zsj_article) 文章模块
- 👏 [zsj_gateway](zsj_gateway) 网关模块
- 🤪 [zsj_webSocket](zsj_webSocket) socket模块
- 💪 [zsj_sms](zsj_sms) 短讯模块

## 🌐 前端地址
    目前已经上线
    www.zsjiscoder.top
    http://47.108.177.31:12345

## 📌 优势及注意事项

```
    使用springcloudAlibaba基础框架
    数据库使用mysql
    缓存使用redis
    rpc使用Feign
    工具类使用hutool
    网关gateway
    分布式锁redisson
    实体工具lombok
    json工具Gson
    配置中心&服务发现nacos
    消息队列rabbitMQ
    邮件服务springmail
```

## 💚 适合人群

    需要有自己的博客的同学
    只需要联系作者加个账号 
    你就可以拥有自己的博客了
    目前作者只做了管理系统界面 (前端不太行)
    客户端网页还没有做 要准备面试了 没什么时间 
    可以自己基于此项目开发一个前端的客户端界面


## 🎇亮点
~~~
    1、使用rabbitmq的direct模式进行消息消费 降低应用耦合度
        日志级别  日志记录到mysql中使用消费者消费log日志队列
        邮件短信级别 邮件发送推送到消息队列 由短信服务进行消费email队列
    2、使用redis缓存读多写少的数据 提高接口并发情况下的存活率
    3、项目数据库每一个服务都是一个不同的数据库 降低单一数据库压力
    4、鉴权由网关入口统一鉴权,根据不同的权限返回不同的信息,并且因为服务发现的统一
       完成了服务之间的动态路由,实现了负载均衡的功能
    5、RPC调用使用Fegin进行操作,封装了简单的接口功能
    6、服务发现与动态配置使用了Alibaba的Nacos,使用@Value注解来动态读取配置信息
    7、项目使用了mp的代码生成器,整合了人人开源的代码生成器(感谢前辈),简化了项目的基础CURD功能的开发
    8、数据库中的密码使用hash512算法进行加密
    9、重要数据的逻辑删除
~~~
