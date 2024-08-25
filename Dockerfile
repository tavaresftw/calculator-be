FROM gradle:7.3.3-jdk11

COPY . /app

WORKDIR /app

RUN gradle build

EXPOSE 8080

CMD ["gradle", "bootRun"]