# WechatAlarm
微信预警(测试公众号)

起因：在使用NIFI的时候，对于流程运行的监控，无论是日志输出还是邮件告警，给我的感觉并不好，短信通知挺好但有资源问题，所以在借鉴了Server酱的思想已经copy了几位前辈的代码后，这块简易的微信告警小工具就出来了。 

此项目调用GitHub第三方授权登陆，封装微信公众号消息模板接口，最终暴露出可以推送微信消息的URL -Get、POST

只要有github账号  微信账号  带域名(80/443端口)，就能快速搭建一个微信预警小工具

## 有以下两者方式获取工具：

1：下载源码，配置application.yml，构建jar包

2：直接下载jar包，配置同级目录下的配置application.yml

## 配置application.yml：

### 1：端口配置必须是80端口(或443) 微信公众号接口必须以 http:// 或 https:// 开头，分别支持80端口和443端口。

![端口配置](https://github.com/nifichina/WechatAlarm/blob/jar/%E7%AB%AF%E5%8F%A3.png)

### 2：配置MySQL数据库

![MySQL配置](https://github.com/nifichina/WechatAlarm/blob/jar/mysql.png)

### 3：配置github 授权登陆

state字段是随机字符串

![github](https://github.com/nifichina/WechatAlarm/blob/jar/github.png)

clientId 与 clientSecret 在你的github账号内获取。点击github中settings，点击developer settings,新建一个oauth app,获得clientId 与 clientSecret 

![](https://github.com/nifichina/WechatAlarm/blob/jar/clientid.png)

在新建的oauth App中配置github 回调地址，其中的URL为指向部署小工具的服务器的域名

![回调](https://github.com/nifichina/WechatAlarm/blob/jar/github%E5%9B%9E%E8%B0%83.png)

### 4：配置域名

微信公众号的开发需要域名，测试公众号还好，若是正式企业公众号需要认证域名

![域名](https://github.com/nifichina/WechatAlarm/blob/jar/%E5%9F%9F%E5%90%8D.png)

### 5：微信配置

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E5%BE%AE%E4%BF%A1.png)

可以使用自己的微信账号注册一个 公众号的测试开发账号，测试账号也足够满足我们小团队的需求的，进入测试账号，会看到appID appsecret，将appid appsecrect配置到application.yml；

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E5%BE%AE%E4%BF%A1appid.png)

然后自定义一个字符串做token，加到application.yml和测试公众号上；接下来需要配置token验证，首先本地先运行小工具(Idea启动也好，java -jar 也好，需要域名)，然后配置公众号上的URL：(https)http://+域名+/weChat/chat  最后点击验证，成功验证就会保存。这里有可能出现一些小问题，排查域名 端口 token URL 

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E5%BE%AE%E4%BF%A1%E9%AA%8C%E8%AF%81.png)

配置js域名，点击保存即可(注意不带http)

![](https://github.com/nifichina/WechatAlarm/blob/jar/js%E5%9F%9F%E5%90%8D.png)

配置模板(发送消息需要模板)：点击新增测试模板，复制粘贴一下内容

预警内容 : {{first.DATA}} 预警时间 : {{keyword1.DATA}}

复制模板id加到application.yml中

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E5%BE%AE%E4%BF%A1%E6%A8%A1%E6%9D%BF.png)

至此全部配置完毕。

## 6 效果

启动：

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E5%90%AF%E5%8A%A8.png)

登陆：

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E7%99%BB%E9%99%86.png)

点击登陆，首次登陆需要登陆github账号并授权，登录后返回公众号二维码 

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E4%BA%8C%E7%BB%B4%E7%A0%81.png)

关注后点击确定，会得到专属你的URL，向这个URL发送get或post请求，消息就会推送到你的微信上了

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E9%A2%84%E8%AD%A6%E6%B6%88%E6%81%AF%E5%8F%91%E9%80%81.png)

![](https://github.com/nifichina/WechatAlarm/blob/jar/%E6%B6%88%E6%81%AF%E6%8E%A5%E6%94%B6.jpg)







