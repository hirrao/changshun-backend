FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/mysql-server:8.0.32

MAINTAINER lengleng(wangiegie@gmail.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./pig.sql /docker-entrypoint-initdb.d

COPY ./pig_config.sql /docker-entrypoint-initdb.d

COPY ./user.sql /docker-entrypoint-initdb.d

COPY ./changshun_server.sql /docker-entrypoint-initdb.d

EXPOSE 3306

# docker build -t pig-mysql-demo:V1 .
# docker run -d --name="pig-mysql-demo-run" -e MYSQL_ROOT_PASSWORD=changshun88 -p 10336:3306 pig-mysql-demo:V1