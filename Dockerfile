#FROM ubuntu
FROM openjdk:8

MAINTAINER gunni

#RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections

#RUN apt-get install -y oracle-java8-installer

COPY target/universal/duelbackend-1.0.zip /tmp/

RUN mkdir -p /srv/duelbackend && \
  unzip -q /tmp/duelbackend-1.0.zip -d /srv/duelbackend && \
  rm /tmp/duelbackend-1.0.zip

RUN chmod +x /srv/duelbackend/duelbackend-1.0/bin/duelbackend

EXPOSE 9000

CMD [ "/srv/duelbackend/duelbackend-1.0/bin/duelbackend" ]