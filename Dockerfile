# 基础镜像
FROM openjdk:11
#作者信息   可以不写！！！
MAINTAINER skean
#申明一个环境变量   可以不写！！！
#ENV HOME_PATH /home
#指定容器启动时，执行命令会在该目录下执行   可以不写！！！
#WORKDIR $HOME_PATH
#应用构建成功后的jar复制到容器指定目录下   上面都不写的就把$HOME_PATH删除！！！！ food_environment_team必须和下面的Image tag生成的镜像名称一样  -0.0.1这个是版本号可以不一样 -SNAPSHOT.jar是固定写法
ADD target/SynologyPhotosUtil-0.0.1-SNAPSHOT.jar /spu.jar
#指定容器内部端口   可以不写，不写默认是项目中pom文件的端口
EXPOSE 8099
#容器启动时执行的命令
ENTRYPOINT ["java","-jar","/spu.jar"]
