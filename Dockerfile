FROM java:8

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY . /usr/src/app

EXPOSE 8080

CMD java -jar target/lmtube.jar