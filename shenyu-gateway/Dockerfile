# 指定 java 环境镜像
FROM java:8
# 复制文件到容器
COPY target/*.jar /application.jar

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT ["java", "-jar", "-Xms256m", "-Xmx256m", "/application.jar"]
