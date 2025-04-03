# ベースイメージは Java 8
FROM openjdk:21

WORKDIR /root/.local

RUN apt-get update

# Eclipse(STS)をインストール
RUN wget https://download.springsource.com/release/STS4/4.6.0.RELEASE/dist/e4.15/spring-tool-suite-4-4.6.0.RELEASE-e4.15.0-linux.gtk.x86_64.tar.gz -O - | tar zxvf - && \
    ln -s /root/.local/sts-4.6.0.RELEASE/SpringToolSuite4 /usr/sbin/sts
# Eclipseは「libswt-gtk-4-jni」がないと動かない
RUN apt-get install libswt-gtk-4-jni -y
# Eclipseの日本語化
#RUN wget https://ftp.jaist.ac.jp/pub/mergedoc/pleiades/build/old/2021/0507/pleiades_20210507.zip && \
#     unzip /root/.local/pleiades_20210507.zip -d pleiades && \
#     cp /root/.local/pleiades/plugins /root/.local/sts-4.6.0.RELEASE -r && \
#     cp /root/.local/pleiades/features /root/.local/sts-4.6.0.RELEASE -r && \
#     echo "-Xverify:none" >> /root/.local/sts-4.6.0.RELEASE/SpringToolSuite4.ini && \
#     echo "-javaagent:/root/.local/sts-4.6.0.RELEASE/plugins/jp.sourceforge.mergedoc.pleiades/pleiades.jar" >> /root/.local/sts-4.6.0.RELEASE/SpringToolSuite4.ini

# X Window Systemのインストール
RUN apt-get install x11-apps -y
ENV DISPLAY=host.docker.internal:0.0

# 日本語化
RUN apt-get install task-japanese -y && \
    apt-get install locales -y && \
    echo "ja_JP.UTF-8 UTF-8" >> /etc/locale.gen && \
    /usr/sbin/locale-gen