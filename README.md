## 大众博客

### 开发者

@[朱远](https://gitee.com/zhu-yuanthorn11z/)<br/>
@[郑声军](https://gitee.com/zhengshengjun/)
<div>
<h1 style="font-family: fantasy;color: cyan"> ZSJBLOG</h1>
</div>


---

## 🎉 模块
- 💪 [zsj_common](zsj_common)  公共模块
- 💅 [zsj_system](zsj_system)   系统模块
- 🌍 [zsj_article](zsj_article) 文章模块
- 👏 [zsj_gateway](zsj_gateway) 网关模块
- 🤪 [zsj_webSocket](zsj_webSocket) socket模块
- 💪 [zsj_sms](zsj_sms) 短讯模块
- 💌 [zsj_task](zsj_task) 定时任务模块
- 💖 [Test README](renren-generator) 代码生成器模块
- 🤗 [sentinel-dashboard](sentinel-dashboard) 资源控制模块

## 🌐 项目地址
    http://47.108.177.31:12345

## 📌 技术栈

```
    使用SpringCloudAlibaba框架
    数据库使用Mysql
    缓存使用Redis
    Rpc使用Feign
    工具类使用Hutool
    网关Gateway
    实体工具Lombok
    Json工具Gson
    配置中心&服务发现Nacos
    消息队列RabbitMQ
    邮件服务SpringMail
    服务限流与熔断sentinel
    分布式锁实现Redission
```

## 💚 适合人群

    需要有自己的博客的同学
    只需要联系作者加个账号 
    你就可以拥有自己的博客了
    目前作者只做了管理系统界面 (前端不太行)
    客户端网页还没有做 要准备面试了 没什么时间 
    可以自己基于此项目开发一个前端的客户端界面


## 🎇亮点以及特性
~~~
    1、使用rabbitmq的direct模式进行消息消费 降低应用耦合度
        日志级别  日志记录到mysql中使用消费者消费log日志队列
        邮件短信级别 邮件发送推送到消息队列 由短信服务进行消费email队列
    2、使用redis缓存读多写少的数据 提高接口并发情况下的存活率
    3、项目数据库每一个服务都是一个不同的数据库 水平分库降低单一数据库压力
    4、鉴权由网关入口统一鉴权,根据不同的权限返回不同的信息,并且因为服务发现的统一
       完成了服务之间的动态路由,实现了负载均衡的功能
    5、RPC调用使用Fegin进行操作,封装了简单的接口功能
    6、服务发现与动态配置使用了Alibaba的Nacos,使用@Value注解来动态读取配置信息
    7、项目使用了mp的代码生成器,整合了人人开源的代码生成器(感谢前辈),简化了项目的基础CURD功能的开发
    8、数据库中的密码使用hash512算法进行加密
    9、引入阿里云存储OSS 完成了服务端签名直传接口 以及文件单点上传接口
    10、完成了数据的Excel的导出功能（EasyExcel）
    11、重要数据的逻辑删除
    12、项目在数据处理的时候使用了一些简单的算法
    13、项目使用前后端分离式的开发,所有的接口都是restful的风格
    14、实现了sentinel的持久化规则
    15、使用分布式锁更好控制数据原子性
~~~

## 🎃日后开发拓展
1、~~引入sentinel进行限流 <br/>~~
2、~~使用redisson中的信号量、读写锁进行数据保护限流功能<br/>~~
3、引入elasticSearch进行日志存储<br/>
4、使用cannel和消息队列进行异步修改数据库<br/>
5、数据库使用集群方式，读写分离,主从方式部署<br/>
6、封装一些好玩的三方接口以供小伙伴们来访问<br/>
    文生图/图生图(stable-diffusion)<br/>
    图片加入水印<br/>
    .............<br/>
7、~~引入一些危险的余额积分操作,数据库的事务操作,或者更高级别的重锁来进行业务的开发<br/>~~
8、使用Jenkins进行cicd部署,提高开发效率    <br/>
9、(tips)以上功能的开发可能是遥遥无期的,你可以尝试催一下作者<br/>

##  ✅合作
联系作者 <br/>
需要一个强有力的前端开发小伙伴(当然后端也行)