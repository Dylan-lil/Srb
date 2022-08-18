# 项目环境

|   环境   |  版本号  |
| :------: | :------: |
| Node.js  | v14.16.1 |
|  MySQL   |  5.7.34  |
|   JDK    |   1.8    |
|  Maven   |  3.6.3   |
|  Nacos   |  1.4.0   |
|  Redis   |  6.2.3   |
| RabbitMQ |  3.8.1   |

# 环境搭建

我们的首要目的是先让项目跑起来，所以接下来将介绍项目中用到的各种环境的搭建方法。

## Redis

如果自己会搭建Redis环境的可以跳过这一节，如果不会的，我建议你使用Docker进行环境搭建，所以我们首先需要在CentOS7下安装Docker。

首先卸载旧版本的Docker：

```shell
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

然后安装Docker依赖：

```shell
sudo yum install -y yum-utils
```

接着配置地址：

```shell
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```

此时即可安装Docker：

```shell
sudo yum install docker-ce docker-ce-cli containerd.io
```

安装完成后启动Docker服务：

```shell
sudo systemctl start docker
```

最后让Docker开启自启动：

```shell
sudo systemctl enable docker
```

---

然后安装Redis，首先下载Redis的镜像：

```shell
docker pull redis
```

创建目录结构：

```shell
mkdir -p /mydata/redis/conf
touch /mydata/redis/conf/redis.conf
```

创建Redis的实例并启动：

```shell
docker run -p 6379:6379 --name redis\
                  -v /mydata/redis/data:/data\
                  -v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf\
                  -d redis redis-server /etc/redis/redis.conf
```

使用该命令即可操作Redis客户端：

```shell
docker exec -it redis redis-cli
```

现在的Redis是不支持数据持久化的，所以来到/mydata/redis/conf目录下，修改redis.conf文件：

```shell
appendonly yes
```

然后重启Redis：

```shell
docker restart redis
```

配置一下使Redis随着Docker的启动而启动：

```shell
docker update redis --restart=always
```

---

现在我们将CentOS7的防火墙关闭，以免产生一些不必要的麻烦：

```shell
systemctl stop firewalld
```

## RabbitMQ

仍然使用Docker下载RabbitMQ：

```shell
docker pull rabbitmq:3.8.1-management
```

创建目录结构：

```shell
cd /mydata
mkdir -p /rabbitmq/data
```

然后启动RabbitMQ：

```shell
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -v /mydata/rabbitmq/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.8.1-management
```

让RabbitMQ随着Docker的启动而启动：

```shell
docker update rabbitmq --restart=always
```

访问[http://192.168.56.10:15672/#/users](http://192.168.56.10:15672/#/users)：

![img](https://img-blog.csdnimg.cn/20210514124725232.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

用户名和密码均为`admin` ，进入到后台管理页面后，添加一个用户：

![img](https://img-blog.csdnimg.cn/20210514124927616.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

如果你没有学习过RabbitMQ的相关知识，请保持和我的配置一致，点击左下角的`Add user` 即可完成添加。

然后添加一下虚拟机：

![img](https://img-blog.csdnimg.cn/20210514125239528.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

需要注意`/srb-host` 前面的`/` 是一定要有的，填写完成后点击`Add virtual host` ，接着点击创建好的用户进行权限设置：

![img](https://img-blog.csdnimg.cn/202105141254048.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

此处选择`my_vhost`：

![img](https://img-blog.csdnimg.cn/20210514125438664.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

最后点击`Set permission`即可，以同样的方式将`/srb-host`虚拟机权限也添加进去：

![img](https://img-blog.csdnimg.cn/20210514125959473.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

## Nacos

本项目使用了Nacos作为服务的注册中心，所以我们需要下载Nacos，下载地址：[https://github.com/alibaba/nacos/releases/tag/1.4.0](https://github.com/alibaba/nacos/releases/tag/1.4.0)，网页拉到底部：

![img](https://img-blog.csdnimg.cn/20210512205355786.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

下载以tar.gz为后缀的压缩包，若是想将其部署在Windows上，就下载zip压缩包，部署方式是一样的。

下载完成后将其上传至CentOS7，并启动（在Nacos的bin目录下启动）：

```shell
cd nacos
cd bin
./startup.sh -m standalone
```

Nacos的启动需要JDK的支持，所以你的CentOS7中必须有JDK的环境，至于JDK的环境在这里就不赘述了。

## 阿里云短信服务

本项目对接了阿里云的短信服务，所以我们需要在阿里云开通一下短信服务，来到官网：[https://www.aliyun.com/](https://www.aliyun.com/)，在首页搜索短信服务，进入短信服务控制台：

![img](https://img-blog.csdnimg.cn/20210512210105108.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

然后进行短信申请：

![img](https://img-blog.csdnimg.cn/20210512210159976.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

不过阿里云平台对短信的申请审核力度非常大，很有可能你是申请不到短信服务的，至于怎么解决这一问题我们一会说。

## 阿里云OSS对象存储服务

本项目对接了阿里云的OSS对象存储服务用于实现图片的存取，开通方式也非常简单，首页搜索对象存储，点击对象存储OSS控制台：

![img](https://img-blog.csdnimg.cn/20210512210521922.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

按照提示进行开通即可，至于接入细节后面再说。

# 启动项目

后台接口和支付系统都是使用SpringBoot开发的项目，后台管理页面和用户前端页面是使用Vue + ElementUI进行开发的，我们先来启动一下前端项目，将前端项目克隆到本地后，执行指令：

```shell
npm install
npm run dev
```

这就是后台管理页面：

![img](https://img-blog.csdnimg.cn/20210512211426335.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

它将管理本系统中的所有数据，然后以同样的方式启动用户前端项目：

![img](https://img-blog.csdnimg.cn/20210512211601978.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

接下来就该启动后台接口项目了，在启动之前，我们需要修改一些配置，首先修改`service-core` 服务下的`application.yml` 文件：

![img](https://img-blog.csdnimg.cn/20210514134130985.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

然后修改`service-gateway`服务的配置文件：

![img](https://img-blog.csdnimg.cn/20210512212501588.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

接着是`service-oss`服务的配置文件：

![img](https://img-blog.csdnimg.cn/2021051221243530.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

这里需要配置阿里云对象存储服务的相关内容，所以我们打开阿里云的对象存储服务控制台，点击左侧的Bucket列表：

![img](https://img-blog.csdnimg.cn/20210512212552995.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

选择创建一个bucket：

![img](https://img-blog.csdnimg.cn/20210512212647253.png)

Bucket名称可以随意填写，读写权限选择公共读：

![img](https://img-blog.csdnimg.cn/20210512212814805.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

创建完成后会得到一个Bucket，点击创建好的Bucket：

![img](https://img-blog.csdnimg.cn/20210512212956256.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

点击左侧的概览：

![img](https://img-blog.csdnimg.cn/20210512213027211.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

此处便会有endpoint的属性值：

![img](https://img-blog.csdnimg.cn/20210512213122469.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

而keyId和keySecret是你在创建对象存储服务时需要创建一个子账户，这就是子账户的keyId和keySecret，bucketName就是刚刚创建Bucket时填写的Bucket名称。

这里需要注意的是，子账户需要添加OSS对象存储服务的权限：

![img](https://img-blog.csdnimg.cn/2021051418341184.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

![img](https://img-blog.csdnimg.cn/20210514183437310.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

包括下面的短信服务，也是需要开通权限的。

最后我们来修改`service-sms` 服务的配置文件：

![img](https://img-blog.csdnimg.cn/20210514134324786.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

将申请好的短信模板编号和名称填入即可，若是无法申请到短信，则我们可以在Redis中直接查看验证码，因为验证码会被存储到Redis中。

由于短信发送需要支付一定的费用，所以默认关闭了短信发送的功能，如果想要开启，可以来到`ApiSmsController`，放开send()方法中的这一行代码注释即可：

![img](https://img-blog.csdnimg.cn/20210514173946850.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

最后启动支付项目，它为整个系统提供了支付功能，该项目同样需要修改配置文件：

![img](https://img-blog.csdnimg.cn/20210512213748639.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyNDUzMTE3,size_16,color_FFFFFF,t_70)

全部修改完成后，我们需要将数据库表导入一下，在这两个项目的根路径下分别有一个`.sql`文件，将它们分别导入一下，最后就可以启动这两个项目了，启动的服务如下：

* ServiceCoreApplication
* ServiceGatewayApplication
* ServiceOssApplication
* ServiceSmsApplication
* ManageApplication
