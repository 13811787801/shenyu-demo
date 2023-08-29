# shenyu-demo
Apache ShenYu Demo
前言
Apache ShenYu（神禹）是采用Java WebFlux编写的响应式API网关，具有异步、高性能、跨语言的特点。

支持HTTP、Dubbo、 Spring Cloud、 gRPC、 Motan、 Sofa、 Tars 等协议
采用插件化设计思想，插件热插拔，易扩展
灵活的流量筛选，能满足各种流量控制
内置鉴权、限流、熔断、防火墙等插件
流量配置动态化，性能极高
支持集群部署，支持A/B测试、蓝绿发布
架构图
image.png

推荐内容




快速部署
Apache Shenyu 支持本地部署、单机快速部署、二进制包部署、Docker部署、K8s部署、集群部署等，为了能够快速体验，我们使用本地部署，同时也为了方便后期看源码。

环境准备
本地正确安装JDK1.8+
本地正确安装Git
本地正确安装Maven
选择一款开发工具，比如IDEA
下载编译代码
从https://github.com/apache/shenyu.git下载最新仓库，下载完成后使用mvn clean install -Dmaven.javadoc.skip=true -B -Drat.skip=true -Djacoco.skip=true -DskipITs -DskipTests编译打包一下。
导入IDEA，切换分支到最小的release版本，我这目前为2.5.0-release
使用开发工具启动 org.apache.shenyu.admin.ShenyuAdminBootstrap，访问 http://localhost:9095 ， 默认用户名和密码分别为: admin 和 123456（默认使用H2数据库），这是Shenyu运维管理后台。
使用开发工具启动org.apache.shenyu.bootstrap.ShenyuBootstrapApplication，这就相当于微服务网关
如下是管理后台截图：

image.png

官网的git地址有误：https://github.com/apache/incubator-shenyu.git

微服务集成
前期准备：nacos服务，搭建步骤看nacos官网
版本关系：

<shenyu.version>2.4.3</shenyu.version>
<boot.version>2.2.2.RELEASE</boot.version>
<cloud.version>Hoxton.SR8</cloud.version>
<nacos.version>2.2.6.RELEASE</nacos.version>
版本管理（统一加到父级pom中）：

<dependencyManagement>
    <dependencies>
        <dependency>
            <!-- Import dependency management from Spring Boot -->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
创建网关
创建网关服务shenyu-gateway,依赖的pom.xml如下
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!--shenyu-->
<!--shenyu gateway start-->
<dependency>
    <groupId>org.apache.shenyu</groupId>
    <artifactId>shenyu-spring-boot-starter-gateway</artifactId>
    <version>${shenyu.version}</version>
</dependency>

<dependency>
    <groupId>org.apache.shenyu</groupId>
    <artifactId>shenyu-spring-boot-starter-plugin-springcloud</artifactId>
    <version>${shenyu.version}</version>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-commons</artifactId>
    <version>2.2.5.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.apache.shenyu</groupId>
    <artifactId>shenyu-spring-boot-starter-plugin-httpclient</artifactId>
    <version>${shenyu.version}</version>
</dependency>

<dependency>
    <groupId>org.apache.shenyu</groupId>
    <artifactId>shenyu-spring-boot-starter-instance</artifactId>
    <version>${shenyu.version}</version>
</dependency>

<!--nacos-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>${nacos.version}</version>
</dependency>

<!-- apache shenyu data sync start use nacos-->
<dependency>
    <groupId>org.apache.shenyu</groupId>
    <artifactId>shenyu-spring-boot-starter-sync-data-nacos</artifactId>
    <version>${shenyu.version}</version>
</dependency>
application.yml配置如下：
server:
  port: 7000

spring:
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
    discovery:
      enabled: true
  main:
    allow-bean-definition-overriding: true
    allow-circular-references: true
  codec:
      max-in-memory-size: 2MB
  application:
    name: shenyu-gateway
    
shenyu:
  sync:
    nacos:
      url: ${spring.cloud.nacos.server-addr}
      # url: 配置成你的 nacos地址，集群环境请使用（,）分隔。
      namespace: sync-data

GatewayApplication代码
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
通常如上三步，已经集成了shenyu-gateway服务。
启动完成日志，可以清楚的看到Shenyu的Logo：

2022-08-26 00:42:11.706  INFO 2300 --- [           main] org.apache.shenyu.web.logo.ShenyuLogo    : 


   _____ _                            
  / ____| |                           
 | (___ | |__   ___ _ __  _   _ _   _ 
  \___ \| '_ \ / _ \ '_ \| | | | | | |
  ____) | | | |  __/ | | | |_| | |_| |
 |_____/|_| |_|\___|_| |_|\__, |\__,_|
                           __/ |      
                          |___/       
 :: Shenyu :: (v2.4.3)


  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.2.RELEASE)

2022-08-26 00:42:13.111  INFO 2300 --- [           main] c.s.demo.gateway.GatewayApplication      : No active profile set, falling back to default profiles: default
...省略
2022-08-26 00:42:21.644  INFO 2300 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port(s): 7000
2022-08-26 00:42:22.363  INFO 2300 --- [           main] c.a.c.n.registry.NacosServiceRegistry    : nacos registry, DEFAULT_GROUP shenyu-gateway 192.168.0.115:7000 register finished
2022-08-26 00:42:23.104  INFO 2300 --- [           main] c.s.demo.gateway.GatewayApplication      : Started GatewayApplication in 12.584 seconds (JVM running for 13.613)
💡 配置依赖的坑，已经给你踩好了，务必按照如上配置

移植ShenYu管理后台
从Apache ShenYu源码复制一份
修改application.yml的注册地址和同步地址，完整配置如下

server:
  port: 9095
  address: 0.0.0.0

spring:
  profiles:
    active: h2
  thymeleaf:
    cache: true
    encoding: utf-8
    enabled: true
    prefix: classpath:/static/
    suffix: .html

mybatis:
  config-location: classpath:/mybatis/mybatis-config.xml
  mapper-locations: classpath:/mappers/*.xml

shenyu:
  register:
    registerType: nacos #http #zookeeper #etcd #nacos #consul
    serverLists: 127.0.0.1:8848 #localhost:2181 #http://localhost:2379 localhost:8848
    props:
      sessionTimeout: 5000
      connectionTimeout: 2000
      checked: true
      zombieCheckTimes: 5
      scheduledTime: 10
      nacosNameSpace: ShenyuRegisterCenter
  sync:
#    websocket:
#      enabled: true
#      messageMaxSize: 10240
#      zookeeper:
#        url: localhost:2181
#        sessionTimeout: 5000
#        connectionTimeout: 2000
#      http:
#        enabled: true
      nacos:
        url: 127.0.0.1:8848
        namespace: sync-data
#        username:
#        password:
#        acm:
#          enabled: false
#          endpoint: acm.aliyun.com
#          namespace:
#          accessKey:
#          secretKey:
#    etcd:
#      url: http://localhost:2379
#    consul:
#      url: http://localhost:8500
  ldap:
    enabled: false
    url: ldap://xxxx:xxx
    bind-dn: cn=xxx,dc=xxx,dc=xxx
    password: xxxx
    base-dn: ou=xxx,dc=xxx,dc=xxx
    object-class: person
    login-field: cn
  jwt:
    expired-seconds: 86400000
  shiro:
    white-list:
      - /
      - /favicon.*
      - /static/**
      - /index**
      - /platform/login
      - /websocket
      - /error
      - /actuator/health
      - /swagger-ui.html
      - /webjars/**
      - /swagger-resources/**
      - /v2/api-docs
      - /csrf
  swagger:
    enable: true

logging:
  level:
    root: info
    org.springframework.boot: info
    org.apache.ibatis: info
    org.apache.shenyu.bonuspoint: info
    org.apache.shenyu.lottery: info
    org.apache.shenyu: info

💡 ShenYu的管理后台不需要做什么修改，只要调整配置即可

创建服务
为了测试效果，我们创建一个server1的服务，一会在Docker中多实例部署

pom.xml依赖
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.shenyu</groupId>
        <artifactId>shenyu-spring-boot-starter-client-springcloud</artifactId>
        <version>${shenyu.version}</version>
    </dependency>

    <!--nacos-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        <version>${nacos.version}</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-commons</artifactId>
        <version>2.2.5.RELEASE</version>
    </dependency>
</dependencies>
application.yml配置
server:
  port: 8080

spring:
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
    discovery:
      enabled: true
  main:
    #允许覆盖bean
    allow-bean-definition-overriding: true
  codec:
    #编码器缓存限制
    max-in-memory-size: 2MB
  application:
    name: server1

shenyu:
  register:
    registerType: nacos #zookeeper #etcd #nacos #consul
    serverLists: ${spring.cloud.nacos.server-addr} #localhost:2181 #http://localhost:2379 #localhost:8848
    props:
      nacosNameSpace: ShenyuRegisterCenter
  client:
    springCloud:
      props:
        contextPath: /server1

创建一个测试接口
@RestController
public class HelloController {

    @PostMapping("/hello")
    public String hello() {
        log.info("ip:{}", InetAddress.getLocalHost().getHostAddress());
        return "Server1 Hello ShenYu";
    }
}
Docker配置
# 指定 java 环境镜像
FROM java:8
# 复制文件到容器
COPY target/*.jar /application.jar

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT ["java", "-jar", "-Xms256m", "-Xmx256m", "/application.jar"]

体验
1.分别启动shenyu-gateway、shenyu-admin、docker部署两个server1实例
image.png

配置路由测试
image.png

配置意思是：当请求头server=server1时，会请求到172.17.0.4 这个服务上
同理配置server2

image.png
配置rule，ruel的规则我们配置为都通过，使用or

image.png
使用Postman请求网关地址[http://localhost:7000/hello](http://localhost:63193/hello)，成功返回了结果Server1 Hello ShenYu

image.png
同时shenyu-gateway打印了路由日志

INFO 1 --- [u-netty-epoll-3] o.a.s.plugin.base.AbstractShenyuPlugin   : divide selector success match , selector name :server1
INFO 1 --- [u-netty-epoll-3] o.a.s.plugin.base.AbstractShenyuPlugin   : divide rule success match , rule name :full rule
INFO 1 --- [u-netty-epoll-3] o.a.s.p.h.AbstractHttpClientPlugin       : The request urlPath is http://172.17.0.4:8080/hello, retryTimes is 3, retryStrategy is current
